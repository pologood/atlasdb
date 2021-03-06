/*
 * Copyright 2015 Palantir Technologies, Inc. All rights reserved.
 *
 * Licensed under the BSD-3 License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.palantir.atlasdb.schema.stream;

import java.io.File;

import com.palantir.atlasdb.keyvalue.api.Namespace;
import com.palantir.atlasdb.schema.AtlasSchema;
import com.palantir.atlasdb.table.description.Schema;
import com.palantir.atlasdb.table.description.TableDefinition;
import com.palantir.atlasdb.table.description.ValueType;

public class StreamTestSchema implements AtlasSchema {
    public static final AtlasSchema INSTANCE = new StreamTestSchema();

    private static final Schema STREAM_TEST_SCHEMA = generateSchema();

    private static Schema generateSchema() {
        Schema schema = new Schema("StreamTest",
                StreamTest.class.getPackage().getName() + ".generated",
                Namespace.DEFAULT_NAMESPACE);

        // stores a mapping from key to streamId
        schema.addTableDefinition("lookup", new TableDefinition() {
            {
                javaTableName("KeyValue");

                rowName();
                rowComponent("key", ValueType.STRING);

                columns();
                column("streamId", "s", ValueType.VAR_LONG);
            }
        });

        // test defaults
        schema.addStreamStoreDefinition(new StreamStoreDefinitionBuilder("stream_test", "stream_test", ValueType.VAR_LONG).build());

        // test all the things!
        schema.addStreamStoreDefinition(
                new StreamStoreDefinitionBuilder("stream_test_with_hash", "stream_test_with_hash", ValueType.VAR_LONG)
                    .inMemoryThreshold(4000)
                    .compressBlocksInDb()
                    .compressStreamInClient()
                    .hashFirstRowComponent()
                    .isAppendHeavyAndReadLight()
                    .build());

        schema.addStreamStoreDefinition(
                new StreamStoreDefinitionBuilder("stream_test_max_mem", "stream_test_max_mem", ValueType.VAR_LONG)
                    .inMemoryThreshold(StreamStoreDefinition.MAX_IN_MEMORY_THRESHOLD)
                    .build());

        return schema;
    }

    public static Schema getSchema() {
        return STREAM_TEST_SCHEMA;
    }

    public static void main(String[]  args) throws Exception {
        STREAM_TEST_SCHEMA.renderTables(new File("src/test/java"));
    }

    @Override
    public Schema getLatestSchema() {
        return STREAM_TEST_SCHEMA;
    }

    @Override
    public Namespace getNamespace() {
        return Namespace.DEFAULT_NAMESPACE;
    }
}
