/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

export class FileUtils {
  public static downloadFile(data: any, options?: string, name?: string) {
    const fileName = name || data.headers.get('content-disposition').match(/filename="(.+)"/)[1];
    const body = options || data.body;
    const blob = new Blob([body]);
    const url = window.URL.createObjectURL(blob);

    if (navigator.msSaveOrOpenBlob) {
      navigator.msSaveBlob(blob, fileName);
    } else {
      const a = document.createElement('a');
      a.href = url;
      a.download = fileName;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
    }
    window.URL.revokeObjectURL(url);
  }
}
