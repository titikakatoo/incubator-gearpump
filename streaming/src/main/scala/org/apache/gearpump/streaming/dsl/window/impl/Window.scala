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
package org.apache.gearpump.streaming.dsl.window.impl

import java.time.Instant

import akka.actor.ActorSystem
import org.apache.gearpump.cluster.UserConfig
import org.apache.gearpump.streaming.Constants._
import org.apache.gearpump.streaming.Processor
import org.apache.gearpump.{Message, TimeStamp}
import org.apache.gearpump.streaming.dsl.window.api._
import org.apache.gearpump.streaming.task.Task

object Window {
  def ofEpochMilli(startTime: TimeStamp, endTime: TimeStamp): Window = {
    Window(Instant.ofEpochMilli(startTime), Instant.ofEpochMilli(endTime))
  }
}

/**
 * A window unit including startTime and excluding endTime.
 */
case class Window(startTime: Instant, endTime: Instant) extends Comparable[Window] {

  /**
   * Returns whether this window intersects the given window.
   */
  def intersects(other: Window): Boolean = {
    startTime.isBefore(other.endTime) && endTime.isAfter(other.startTime)
  }

  /**
   * Returns the minimal window that includes both this window and
   * the given window.
   */
  def span(other: Window): Window = {
    Window(Instant.ofEpochMilli(Math.min(startTime.toEpochMilli, other.startTime.toEpochMilli)),
      Instant.ofEpochMilli(Math.max(endTime.toEpochMilli, other.endTime.toEpochMilli)))
  }

  override def compareTo(o: Window): Int = {
    val ret = startTime.compareTo(o.startTime)
    if (ret != 0) {
      ret
    } else {
      endTime.compareTo(o.endTime)
    }
  }
}


