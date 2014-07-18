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
package com.sindicetech.siren.solr.qparser.tree;

import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QueryParsing;

import com.sindicetech.siren.solr.qparser.SirenQParser;
import com.sindicetech.siren.solr.qparser.SirenQParserPlugin;
import com.sindicetech.siren.solr.schema.ConciseTreeField;
import com.sindicetech.siren.solr.schema.ExtendedTreeField;

/**
 * Implementation of {@link com.sindicetech.siren.solr.qparser.SirenQParserPlugin} for the
 * {@link TreeQParser}.
 */
public class TreeQParserPlugin extends SirenQParserPlugin {

  @Override
  public QParser createParser(final String qstr, final SolrParams localParams,
                              final SolrParams params, final SolrQueryRequest req) {

    SirenQParser parser = new TreeQParser(qstr, localParams, params, req);
    parser.setQNames(this.getQNames());

    return parser;
  }

}
