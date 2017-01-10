/**
 * Copyright 2017 Palantir Technologies
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
package com.palantir.atlasdb.timelock.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Test;

public class PaxosConfigurationTest {
    private static final long POSITIVE_LONG = 100L;
    private static final long ZERO_LONG = 0L;
    private static final long NEGATIVE_LONG = -100L;

    @Test
    public void canCreateWithDefaultValues() {
        ImmutablePaxosConfiguration defaultConfiguration = ImmutablePaxosConfiguration.builder().build();
        assertThat(defaultConfiguration).isNotNull();
    }

    @Test
    public void canCreateDirectoryForPaxosDirectory() {
        File mockFile = mock(File.class);
        when(mockFile.exists()).thenReturn(false);
        when(mockFile.mkdirs()).thenReturn(true);
        ImmutablePaxosConfiguration.builder()
                .paxosDataDir(mockFile)
                .build();
    }

    @Test
    public void canUseExistingDirectoryAsPaxosDirectory() {
        File mockFile = mock(File.class);
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.mkdirs()).thenReturn(false);
        ImmutablePaxosConfiguration.builder()
                .paxosDataDir(mockFile)
                .build();
    }

    @Test
    public void throwsIfCannotCreatePaxosDirectory() {
        File mockFile = mock(File.class);
        when(mockFile.exists()).thenReturn(false);
        when(mockFile.mkdirs()).thenReturn(false);
        assertThatThrownBy(ImmutablePaxosConfiguration.builder()
                .paxosDataDir(mockFile)
                ::build).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void canSpecifyPositivePingRate() {
        ImmutablePaxosConfiguration.builder()
                .pingRateMs(POSITIVE_LONG)
                .build();
    }

    @Test
    public void throwOnNegativePingRate() {
        assertThatThrownBy(ImmutablePaxosConfiguration.builder()
                .pingRateMs(NEGATIVE_LONG)
                ::build).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void throwOnZeroPingRate() {
        assertThatThrownBy(ImmutablePaxosConfiguration.builder()
                .pingRateMs(ZERO_LONG)
                ::build).isInstanceOf(IllegalArgumentException.class);
    }
    @Test
    public void canSpecifyPositiveRandomWaitBeforeProposingLeadership() {
        ImmutablePaxosConfiguration.builder()
                .randomWaitBeforeProposingLeadershipMs(POSITIVE_LONG)
                .build();
    }

    @Test
    public void throwOnNegativeRandomWaitBeforeProposingLeadership() {
        assertThatThrownBy(ImmutablePaxosConfiguration.builder()
                .randomWaitBeforeProposingLeadershipMs(NEGATIVE_LONG)
                ::build).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void throwOnZeroRandomWaitBeforeProposingLeadership() {
        assertThatThrownBy(ImmutablePaxosConfiguration.builder()
                .randomWaitBeforeProposingLeadershipMs(ZERO_LONG)
                ::build).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void canSpecifyPositiveLeaderPingResponseWait() {
        ImmutablePaxosConfiguration.builder()
                .leaderPingResponseWaitMs(POSITIVE_LONG)
                .build();
    }

    @Test
    public void throwOnNegativeLeaderPingResponseWait() {
        assertThatThrownBy(ImmutablePaxosConfiguration.builder()
                .leaderPingResponseWaitMs(NEGATIVE_LONG)
                ::build).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void throwOnZeroLeaderPingResponseWait() {
        assertThatThrownBy(ImmutablePaxosConfiguration.builder()
                .leaderPingResponseWaitMs(ZERO_LONG)
                ::build).isInstanceOf(IllegalArgumentException.class);
    }
}