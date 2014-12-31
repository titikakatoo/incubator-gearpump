/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.gearpump.streaming.transaction.lib.kafka.grouper

import kafka.common.TopicAndPartition
import org.apache.gearpump.streaming.ConfigsHelper._
import org.apache.gearpump.util.Configs

class KafkaDefaultGrouperFactory extends KafkaGrouperFactory {
  override def getKafkaGrouper(conf: Configs): KafkaGrouper = {
    val taskNum = conf.dag.tasks(conf.taskId.groupId).parallelism
    val taskIndex = conf.taskId.index
    new KafkaDefaultGrouper(taskNum, taskIndex)
  }
}

/**
 * default grouper groups TopicAndPartitions among StreamProducers by partitions
 *
 * e.g. given 2 topics (topicA with 2 partitions and topicB with 3 partitions) and
 * 2 streamProducers (streamProducer0 and streamProducer1)
 *
 * streamProducer0 gets (topicA, partition1), (topicB, partition1) and (topicA, partition3)
 * streamProducer1 gets (topicA, partition2), (topicB, partition2)
 */
class KafkaDefaultGrouper(taskNum: Int, taskIndex: Int) extends KafkaGrouper {
  def group(topicAndPartitions: Array[TopicAndPartition]): Array[TopicAndPartition] = {
    0.until(topicAndPartitions.size).filter(_ % taskNum == taskIndex).map(i => topicAndPartitions(i)).toArray
  }
}