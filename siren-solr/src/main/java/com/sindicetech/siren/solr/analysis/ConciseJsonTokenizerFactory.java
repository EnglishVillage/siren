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

package com.sindicetech.siren.solr.analysis;

import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeSource;

import com.sindicetech.siren.analysis.ConciseJsonTokenizer;

import java.io.Reader;
import java.util.Map;

/**
 * Factory for {@link com.sindicetech.siren.analysis.ConciseJsonTokenizer}.
 */
public class ConciseJsonTokenizerFactory extends TokenizerFactory {

  /**
   * Initialize this factory via a set of key-value pairs.
   */
  public ConciseJsonTokenizerFactory(final Map<String, String> args) {
    super(args);
  }

	@Override
	public ConciseJsonTokenizer create(final AttributeSource.AttributeFactory factory, final Reader input) {
		return new ConciseJsonTokenizer(factory, input);
	}

}
