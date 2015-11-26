
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

package org.nlplab.brat.ann;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import org.nlplab.brat.configuration.BratType;

/**
 * @author Rinat Gareev
 */
public class BratStructureAnnotation<BT extends BratType> extends BratAnnotation<BT> {

    private Multimap<String, BratAnnotation<?>> roleAnnotations;

    public BratStructureAnnotation(BT type,
                                   Multimap<String, ? extends BratAnnotation<?>> roleAnnotations) {
        super(type);
        setRoleAnnotations(roleAnnotations);
    }

    public Multimap<String, BratAnnotation<?>> getRoleAnnotations() {
        return roleAnnotations;
    }

    protected void setRoleAnnotations(Multimap<String, ? extends BratAnnotation<?>> roleAnnotations) {
        this.roleAnnotations = ImmutableMultimap.copyOf(roleAnnotations);
    }
}