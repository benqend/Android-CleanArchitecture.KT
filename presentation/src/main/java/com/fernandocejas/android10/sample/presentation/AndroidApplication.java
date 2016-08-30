/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fernandocejas.android10.sample.presentation;

import android.app.Application;

import com.fernandocejas.android10.sample.presentation.weex.extend.adapter.ImageAdapter;
import com.fernandocejas.android10.sample.presentation.weex.extend.component.RichText;
import com.fernandocejas.android10.sample.presentation.weex.extend.module.MyModule;
import com.fernandocejas.android10.sample.presentation.weex.extend.module.RenderModule;
import com.fernandocejas.android10.sample.presentation.weex.extend.module.WXEventModule;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;

/**
 * Android Main Application
 */
public class AndroidApplication extends Application {


  @Override public void onCreate() {
    super.onCreate();
    initWeex();
  }

  private void initWeex() {
    InitConfig config=new InitConfig.Builder().setImgAdapter(new ImageAdapter()).build();
    WXSDKEngine.initialize(this,config);
    try {
      WXSDKEngine.registerComponent("richtext", RichText.class);
      WXSDKEngine.registerModule("render", RenderModule.class);
      WXSDKEngine.registerModule("event", WXEventModule.class);

      WXSDKEngine.registerModule("myModule", MyModule.class);

    } catch (WXException e) {
      e.printStackTrace();
    }
  }
}
