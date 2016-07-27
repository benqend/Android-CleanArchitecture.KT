package com.fernandocejas.android10.sample.presentation.weex.https;

public interface WXRequestListener {

  void onSuccess(WXHttpTask task);

  void onError(WXHttpTask task);
}
