# PreferencesObj
It's sample android save a object to SharePreferences

## How to use?

In your `build.gradle`

```gradle
compile 'com.kylins.libs:preferences-object:1.0.0-beta'
```
### Save object to SharePreferences:
  ```java
  //add data class
  public interface Data {
    void setXXX(int a);
    int getXXX();

    .... String,Set<String>,int,long,boolean,float ....
  }
  
  //Get instance
  Data data = SharePref.getInstance(Data.class);
  Just use data.set to 'set' value and 'get' value.
  ```
  
### Object copy (Support difference class)

  //Annotations:
  
  @Name change the filed from object field name.
  
  @Ignore this filed need't copy.
  
  @DeepCopy this filed will copy value from a instance(Value copy).
 
  //usage:
  ```java
  TargetClass instance = ObjectCopy.copy(TargetClass,source_instance);//TargetClass is your custom class
  TargetClass instance = ObjectCopyTo.copy(TargetClass,source_instance);//source_instance is your custom class
  ```
  
### Reflect Instance
```java
  DynamicObject dynamtic = new DynamicObject("Classname",args);//get instance from a class,args is constructor args.null is don't zero custructor
  dynamtic.call("MethodName")//call
  dynamtic.field("FieldName")//get field with DynamicObject
  ```
  
## License

    Copyright 2015 Square, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


