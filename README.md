weakexecutor
===

weakexecutor is an [Executor](https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/Executor.html) decorator library that wraps its callbacks in [WeakReference](https://docs.oracle.com/javase/7/docs/api/java/lang/ref/WeakReference.html) instances:

```java
final Callback<String> callback = new Callback<String>() {
  @Override
  public void onSuccess(String result) {
    Snackbar.make(view, result, Snackbar.LENGTH_SHORT).show();
  }

  @Override
  public void onFailure(Exception exception) {
    Snackbar.make(view, exception.getMessage(), Snackbar.LENGTH_SHORT).show();
  }
};

final WeakExecutor executor = new SimpleWeakExecutor();
executor.execute(new Task<String>() {
  @Override
  public String execute() throws Exception {
    SystemClock.sleep(2500);
    return "Hello weakexecutor";
  }
}, callback);
```

Although this was written with Android platform in mind, it also works well as a plain Java library. On Android, it supports SDK versions 15 and above. Older versions will be supported with future updates.

To use it, add the following to your dependency list:

```groovy
compile 'com.github.tslamic.weakexecutor:library:0.9'
```

License
---

	Copyright 2016 Tadej Slamic
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	    http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
