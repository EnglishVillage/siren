/**
 * Copyright (c) 2014, Sindice Limited. All Rights Reserved.
 *
 * This file is part of the SIREn project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sindicetech.siren.solr.facet;

/**
 * <p>A holder object for keeping a generated SIREn facet field.
 * 
 * <p>Transforms the information to the name of the new facet via {@link #toFieldName()}.
 * 
 * @experimental Can change in the next release.
 */
public class SirenFacetEntry {
  public String fieldName;
  public String path;
  public Object value;
  public FacetDatatype datatype;
  
  public String toString() {
    return String.format("<%s, %s, %s, %s>", fieldName, path, value.toString(), datatype);
  }
  
  /**
   * Generates the SIREn facet field name.
   * 
   * @return the generated facet field name.
   */
  public String toFieldName() {
    return String.format("%s.%s.%s", datatype.xsdDatatypeLocalPart, fieldName, path);
  }
}
