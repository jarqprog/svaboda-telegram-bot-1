package com.svaboda.storage.failureinfo;

import io.vavr.control.Try;

import java.util.List;

public interface FailureInfoRepository {

    Try<Void> save(FailureInfo failureInfo);

    Try<List<FailureInfo>> loadAll();

}
