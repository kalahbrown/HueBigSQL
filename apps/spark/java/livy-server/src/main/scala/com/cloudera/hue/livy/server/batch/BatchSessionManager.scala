/*
 * Licensed to Cloudera, Inc. under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  Cloudera, Inc. licenses this file
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

package com.cloudera.hue.livy.server.batch

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

import scala.collection.JavaConversions._
import scala.concurrent.Future

class BatchManager(batchFactory: BatchSessionFactory) {
  private[this] val _idCounter = new AtomicInteger()
  private[this] val _batches = new ConcurrentHashMap[Int, BatchSession]

  def getBatch(id: Int): Option[BatchSession] = Option(_batches.get(id))

  def getBatches: Array[BatchSession] = _batches.values().iterator().toArray

  def createBatch(createBatchRequest: CreateBatchRequest): BatchSession = {
    val id = _idCounter.getAndIncrement
    val batch = batchFactory.create(id, createBatchRequest)
    _batches.put(id, batch)

    batch
  }

  def remove(id: Int): Option[BatchSession] = {
    Option(_batches.remove(id))
  }

  def delete(batch: BatchSession): Future[Unit] = {
    _batches.remove(batch.id)
    batch.stop()
  }

  def shutdown() = {

  }
}

case class CreateBatchRequest(file: String,
                              proxyUser: Option[String] = None,
                              args: List[String] = List(),
                              className: Option[String] = None,
                              jars: List[String] = List(),
                              pyFiles: List[String] = List(),
                              files: List[String] = List(),
                              driverMemory: Option[String] = None,
                              driverCores: Option[Int] = None,
                              executorMemory: Option[String] = None,
                              executorCores: Option[Int] = None,
                              archives: List[String] = List())
