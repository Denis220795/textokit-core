/*
 *    Copyright 2015 Textocat
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.textocat.textokit.eval.matching;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;

import java.util.*;

/**
 * Dispatch matching to a submatcher on the basis of reference FS type.
 *
 * @author Rinat Gareev
 */
public class TypeBasedMatcherDispatcher<FST extends FeatureStructure> extends MatcherBase<FST> {

    public static <FST extends FeatureStructure> Builder<FST> builder(TypeSystem ts) {
        return new Builder<FST>(ts);
    }

    public static class Builder<FST extends FeatureStructure> {
        private TypeBasedMatcherDispatcher<FST> instance = new TypeBasedMatcherDispatcher<FST>();

        public Builder<FST> addSubmatcher(Type t, Matcher<FST> m) {
            instance.type2matcher.put(t, m);
            return this;
        }

        Builder(TypeSystem ts) {
            instance.ts = ts;
            instance.type2matcher = Maps.newHashMap();
        }

        public TypeBasedMatcherDispatcher<FST> build() {
            instance.type2matcher = ImmutableMap.copyOf(instance.type2matcher);
            return instance;
        }
    }

    private TypeSystem ts;
    private Map<Type, Matcher<FST>> type2matcher;

    private TypeBasedMatcherDispatcher() {
    }

    public Set<Type> getRegisteredTypes() {
        return ImmutableSet.copyOf(type2matcher.keySet());
    }

    public Matcher<FST> getMatcherFor(Type t) {
        return type2matcher.get(t);
    }

    @Override
    public boolean match(FST ref, FST cand) {
        Matcher<FST> submatcher = getSubmatcher(ref);
        return submatcher.match(ref, cand);
    }

    @Override
    public void print(StringBuilder out, FST value) {
        getSubmatcher(value).print(out, value);
    }

    private Matcher<FST> getSubmatcher(FST ref) {
        Type refType = ref.getType();
        Matcher<FST> submatcher = type2matcher.get(refType);
        while (submatcher == null) {
            refType = ts.getParent(refType);
            if (refType == null) {
                break;
            }
            submatcher = type2matcher.get(refType);
        }
        if (submatcher == null) {
            throw new IllegalStateException(String.format(
                    "There is no submatcher for type %s", refType));
        }
        return submatcher;
    }

    @Override
    protected String toString(IdentityHashMap<Matcher<?>, Integer> idMap) {
        if (type2matcher == null) {
            return "EmptyTypeBasedMatcherDispatcher";
        } else {
            idMap.put(this, getNextId(idMap));
            StringBuilder sb = new StringBuilder("[");
            Iterator<Type> typesIter = type2matcher.keySet().iterator();
            while (typesIter.hasNext()) {
                Type t = typesIter.next();
                sb.append(t).append(" => ");
                sb.append(getToString(idMap, type2matcher.get(t)));
                if (typesIter.hasNext())
                    sb.append(" || ");
            }
            sb.append("]");
            return sb.toString();
        }
    }

    @Override
    protected Collection<Matcher<?>> getSubMatchers() {
        Collection<Matcher<?>> result = new LinkedList<Matcher<?>>();
        result.addAll(type2matcher.values());
        return result;
    }
}
