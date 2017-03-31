/*
 * Copyright 2015 Palantir Technologies
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
package com.palantir.paxos;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.palantir.paxos.persistence.generated.remoting.PaxosAcceptorPersistence;

public class PaxosResponses {
    public static Predicate<PaxosResponse> isSuccessfulPredicate() {
        return new Predicate<PaxosResponse>() {
            @Override
            public boolean apply(@Nullable PaxosResponse response) {
                return response != null && response.isSuccessful();
            }
        };
    }

    public static PaxosAcceptorPersistence.PaxosResponse toProto(PaxosResponse result) {
        return PaxosAcceptorPersistence.PaxosResponse.newBuilder().
                setAck(result.isSuccessful()).
                build();
    }

    public static PaxosResponse fromProto(PaxosAcceptorPersistence.PaxosResponse proto) {
        boolean ack = proto.getAck();
        return new PaxosResponseImpl(ack);
    }
}
