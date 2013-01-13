/*******************************************************************************
 * Copyright (C) 2012 BJ Peter DeLaCruz
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.katas.common;

/**
 * This enum is used especially by the <code>KataUtils</code> class to simplify the creation of
 * methods that return different types of objects.
 * 
 * @author BJ Peter DeLaCruz
 */
public enum KataEnums {

  /** The different data types that an ADT can hold. */
  CHARACTER("Character"), DOUBLE("Double"), INTEGER("Integer"), STRING("String");

  private final String type;

  /**
   * Creates a new KataEnums object with the given value representing a data type, e.g. Integer.
   * 
   * @param type The data type.
   */
  KataEnums(String type) {
    this.type = type;
  }

  /**
   * Gets the value of this KataEnums object, which represents a data type, e.g. Integer.
   * 
   * @return The data type.
   */
  public String getDisplayName() {
    return this.type;
  }
}
