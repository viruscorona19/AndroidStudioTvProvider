package com.android.providers.tv.util;
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.system.ErrnoException;
import android.system.Os;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.RandomAccessFile;
import java.net.Socket;

public final class IoUtils {
  private IoUtils() {
  }
  /**
   * Calls close(2) on 'fd'. Also resets the internal int to -1. Does nothing if 'fd' is null
   * or invalid.
   */
  public static void close(FileDescriptor fd) throws IOException {
    try {
      if (fd != null && fd.valid()) {
        Os.close(fd);

      }
    } catch (ErrnoException errnoException) {
      throw new IOException();
    }
  }
  /**
   * Closes 'closeable', ignoring any checked exceptions. Does nothing if 'closeable' is null.
   */
  public static void closeQuietly(AutoCloseable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (RuntimeException rethrown) {
        throw rethrown;
      } catch (Exception ignored) {
      }
    }
  }
  /**
   * Closes 'fd', ignoring any exceptions. Does nothing if 'fd' is null or invalid.
   */
  public static void closeQuietly(FileDescriptor fd) {
    try {
      IoUtils.close(fd);
    } catch (IOException ignored) {
    }
  }
  /**
   * Closes 'socket', ignoring any exceptions. Does nothing if 'socket' is null.
   */
  public static void closeQuietly(Socket socket) {
    if (socket != null) {
      try {
        socket.close();
      } catch (Exception ignored) {
      }
    }
  }


  /**
   * Recursively delete everything in {@code dir}.
   */
  // TODO: this should specify paths as Strings rather than as Files
  public static void deleteContents(File dir) throws IOException {
    File[] files = dir.listFiles();
    if (files == null) {
      throw new IOException("listFiles returned null: " + dir);
    }
    for (File file : files) {
      if (file.isDirectory()) {
        deleteContents(file);
      }
      if (!file.delete()) {
        throw new IOException("failed to delete file: " + file);
      }
    }
  }

  public static void throwInterruptedIoException() throws InterruptedIOException {
    // This is typically thrown in response to an
    // InterruptedException which does not leave the thread in an
    // interrupted state, so explicitly interrupt here.
    Thread.currentThread().interrupt();
    // TODO: set InterruptedIOException.bytesTransferred
    throw new InterruptedIOException();
  }
}
