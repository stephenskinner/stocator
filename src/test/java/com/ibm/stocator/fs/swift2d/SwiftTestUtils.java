/**
 * (C) Copyright IBM Corp. 2010, 2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.ibm.stocator.fs.swift2d;

import java.io.IOException;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * Utilities used across test cases
 */
public class SwiftTestUtils extends org.junit.Assert {

  /**
   * Read an object from Swift
   *
   * @param fs filesystem
   * @param path object bath
   * @param len how much to read
   * @return byte array
   * @throws IOException if can't read an object
   */
  public static byte[] readDataset(FileSystem fs, Path path, int len) throws IOException {
    FSDataInputStream in = fs.open(path);
    byte[] dest = new byte[len];
    try {
      in.readFully(0, dest);
    } finally {
      in.close();
    }
    return dest;
  }

  public static String formatFilestat(FileStatus[] stats, String separator) {
    StringBuilder buf = new StringBuilder(stats.length * 128);
    for (int i = 0; i < stats.length; i++) {
      buf.append(String.format("[%02d] %s", i, stats[i])).append(separator);
    }
    return buf.toString();
  }

  /**
   * Random dataset
   *
   * @param len length of data
   * @param base base of the data
   * @param modulo the modulo
   * @return the newly generated dataset
   */
  public static byte[] generateDataset(int len, int base, int modulo) {
    byte[] dataset = new byte[len];
    for (int i = 0; i < len; i++) {
      dataset[i] = (byte) (base + (i % modulo));
    }
    return dataset;
  }

  /**
   * Verify if path exists
   *
   * @param fs filesystem to examine
   * @param message message to include in the assertion failure message
   * @param path path in the filesystem
   * @throws IOException IO problems
   */
  public static void assertPathExists(FileSystem fs,
      String message, Path path) throws IOException {
    if (!fs.exists(path)) {
      fail(message + ": not found " + path + " in " + path.getParent());
      SwiftTestUtils.formatFilestat(fs.listStatus(path.getParent()), "\n");
    }
  }

}
