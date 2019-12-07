/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.hasor.dataql.domain;
import net.hasor.dataql.Option;
import net.hasor.dataql.UDF;

/**
 * 函数调用
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2017-03-23
 */
public class UdfModel implements DataModel, UDF {
    private UDF udf = null;

    UdfModel(UDF udf) {
        this.udf = udf;
    }

    @Override
    public UDF asOri() {
        return this.udf;
    }

    /** 判断是否为 UdfModel 类型值 */
    public boolean isUdfModel() {
        return true;
    }

    @Override
    public Object call(Object[] values, Option option) throws Throwable {
        return this.udf.call(values, option);
    }
}