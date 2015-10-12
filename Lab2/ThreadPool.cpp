//
// Created by mereckaj on 10/10/15.
//
#include "ThreadPool.h"

ThreadPool::ThreadPool(int ps) {
    poolSize = ps;
    prepare();
    initThreads();
}
ThreadPool::~ThreadPool() {
    // free memory here
    joinThreads();
    free(threads);
}
void ThreadPool::prepare() {
    pthread_t tmp[poolSize];
    ThreadPool::threads = tmp;
}
void ThreadPool::initThreads() {
    int rc;
    for(long i = 0; i < ThreadPool::poolSize; i++){
        rc = pthread_create(&threads[i],NULL,&ThreadPool::threadWorkerStaticEntry,(void*)i);
        checkReturnCode(rc,"initThread");
    }
}
void* ThreadPool::threadWorkerStaticEntry(void* arg){
    std::cout << "Hi from thread " << (long)arg << std::endl;
//    ((ThreadPool *)arg)->threadWorkder(arg);
    return NULL;
}
void ThreadPool::threadWorkder(void * arg) {
    long n = (long)arg;
    std::cout << "Hi from thread " << n << std::endl;
}
void ThreadPool::joinThreads() {
    int rc;
    for(int i = 0; i < poolSize;i++){
        rc= pthread_join(threads[i],NULL);
        checkReturnCode(rc,"joinThread");
    }
}
void ThreadPool::checkReturnCode(int rc, std::string location){
    if(rc!=0){
        std::cout << "Error occured at " << location << std::endl;
    }
}