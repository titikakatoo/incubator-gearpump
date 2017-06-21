/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.gearpump.streaming.refactor.coder;

import java.util.List;

public class ListCoder<T> extends IterableLikeCoder<T, List<T>> {

    public static <T> ListCoder<T> of(Coder<T> elemCoder) {
        return new ListCoder<>(elemCoder);
    }

    /////////////////////////////////////////////////////////////////////////////
    // Internal operations below here.

    @Override
    protected final List<T> decodeToIterable(List<T> decodedElements) {
        return decodedElements;
    }

    protected ListCoder(Coder<T> elemCoder) {
        super(elemCoder, "List");
    }

    @Override
    public void verifyDeterministic() throws NonDeterministicException {
        verifyDeterministic(this, "ListCoder.elemCoder must be deterministic",
                (Iterable<Coder<?>>)getElemCoder());
    }

}