//
// Created by Jin Jin on 2019-05-13.
//

#include <string.h>
#include <jni.h>
#include <android/log.h>
#include "optimal_filtering-jni.h"

JNIEXPORT jfloat JNICALL
Java_com_huawei_sensormonitor_MainActivity_floatFromJNI(JNIEnv *pEnv, jobject pObj,
                                                         jfloat pFloatP, jfloatArray pArrayP) {
    jboolean isCopy;
    __android_log_print(ANDROID_LOG_DEBUG, "Native c raw data:\n", "%.1f in %d bytes", pFloatP, sizeof(pFloatP));
    jfloat *floatArr = (*pEnv)->GetFloatArrayElements(pEnv, pArrayP, &isCopy);
    __android_log_print(ANDROID_LOG_INFO, "GetReleaseFloatArrayDemo", "a new copy is created: %d", isCopy);

    jsize len = (*pEnv)->GetArrayLength(pEnv, pArrayP);
    int i;
    float sum = 0;
    for (i = 0; i < len; ++i) {
        __android_log_print(ANDROID_LOG_INFO, "GetReleaseFloatArrayDemo ", "%d: %.1f", i, floatArr[i]);
        sum+=floatArr[i];
    }
    (*pEnv)->ReleaseFloatArrayElements(pEnv, pArrayP, floatArr, 0);


    return sum/len;

}
