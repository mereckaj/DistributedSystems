cmake_minimum_required(VERSION 3.3)

project(DistributedSystems)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

find_package(Threads)
set(SOURCE_FILES
    Lab2/Main.cpp
    Lab2/Main.h
    Lab2/ThreadPool.cpp Lab2/ThreadPool.h)

add_executable(DistributedSystems ${SOURCE_FILES})
target_link_libraries (DistributedSystems ${CMAKE_THREAD_LIBS_INIT})
