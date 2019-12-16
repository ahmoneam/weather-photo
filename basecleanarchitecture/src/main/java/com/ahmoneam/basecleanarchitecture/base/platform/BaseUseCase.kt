package com.ahmoneam.basecleanarchitecture.base.platform

abstract class BaseUseCase<Repository : IBaseRepository>(val repository: Repository)