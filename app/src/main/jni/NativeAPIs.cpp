#include "com_htc_eleven_multimediatesttool_NDKDemoActivity.h"
/*
 * Class:     com_htc_eleven_multimediatesttool_NDKDemoActivity
 * Method:    getNativeServiceName
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_htc_eleven_multimediatesttool_NDKDemoActivity_getNativeServiceName
  (JNIEnv *env, jobject obj) {


    return env->NewStringUTF("Hello eleven !");
}
