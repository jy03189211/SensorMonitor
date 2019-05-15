//
// Created by Xi Gong on 2019-05-13.
//

#ifndef SENSORMONITOR_HELLO_JNI_H
#define SENSORMONITOR_HELLO_JNI_H

/* 1 Dimension */
typedef struct {
    float x;  /* state */
    float A;  /* x(n)=A*x(n-1)+u(n),u(n)~N(0,q) */
    float H;  /* z(n)=H*x(n)+w(n),w(n)~N(0,r)   */
    float q;  /* process(predict) noise convariance */
    float r;  /* measure noise convariance */
    float p;  /* estimated error convariance */
    float gain;
} kalman1_state;
extern void kalman1_init(kalman1_state *state, float init_x, float init_p);
extern float kalman1_filter(kalman1_state *state, float z_measure);

#endif //SENSORMONITOR_HELLO_JNI_H
