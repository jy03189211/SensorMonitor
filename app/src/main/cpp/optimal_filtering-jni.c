//
// Created by Jin Jin on 2019-05-13.
//

#include <string.h>
#include <jni.h>
#include <android/log.h>

JNIEXPORT jfloat JNICALL
Java_com_huawei_sensormonitor_MainActivity_floatFromJNI(JNIEnv *pEnv, jobject pObj,
                                                         jfloat pFloatP) {
    __android_log_print(ANDROID_LOG_INFO, "native", "%.1f in %d bytes", pFloatP, sizeof(pFloatP));

/*
    char nativeStr[6];
    jsize length = (*pEnv)->GetStringUTFLength(pEnv, pStringP);
    (*pEnv)->GetStringUTFRegion(pEnv, pStringP, 0, length, nativeStr);
    __android_log_print(ANDROID_LOG_INFO, "native",
                        "jstring converted to UTF-8 string and copied to native buffer: %s that is %d long",
                        nativeStr, length);
*/
    return pFloatP;

}
