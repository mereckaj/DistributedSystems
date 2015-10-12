//
// Created by mereckaj on 10/10/15.
//

#ifndef DISTRIBUTEDSYSTEMS_THREADPOOL_H
#define DISTRIBUTEDSYSTEMS_THREADPOOL_H

#include "Main.h"
class ThreadPool{
public:
    ThreadPool(int pool_size);
    ~ThreadPool();
private:
    pthread_t *threads;
    int poolSize;
    void prepare();
    void initThreads();
    void joinThreads();
    void checkReturnCode(int,std::string);
    static void* threadWorkerStaticEntry(void*);
    void threadWorkder(void*);
};
#endif //DISTRIBUTEDSYSTEMS_THREADPOOL_H
