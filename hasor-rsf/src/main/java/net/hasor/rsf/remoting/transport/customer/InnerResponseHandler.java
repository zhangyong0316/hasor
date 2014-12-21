/*
 * Copyright 2008-2009 the original 赵永春(zyc@hasor.net).
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
package net.hasor.rsf.remoting.transport.customer;
import net.hasor.rsf.RsfFuture;
import net.hasor.rsf.RsfResponse;
import net.hasor.rsf.adapter.AbstractRsfClient;
import net.hasor.rsf.constants.ProtocolStatus;
import net.hasor.rsf.constants.RsfException;
import net.hasor.rsf.remoting.transport.protocol.message.ResponseMsg;
import net.hasor.rsf.utils.RuntimeUtils;
/**
 * 负责处理客户端 Response 回应逻辑。
 * @version : 2014年11月4日
 * @author 赵永春(zyc@hasor.net)
 */
class InnerResponseHandler implements Runnable {
    private ResponseMsg       responseMsg;
    private AbstractRsfClient rsfClient;
    private RsfFuture         rsfFuture;
    //
    public InnerResponseHandler(ResponseMsg responseMsg, AbstractRsfClient rsfClient, RsfFuture rsfFuture) {
        this.responseMsg = responseMsg;
        this.rsfClient = rsfClient;
        this.rsfFuture = rsfFuture;
    }
    public void run() {
        //状态判断
        short resStatus = responseMsg.getStatus();
        if (resStatus == ProtocolStatus.Accepted) {
            //
            return;
        } else if (resStatus == ProtocolStatus.ChooseOther) {
            //
            this.rsfClient.tryAgain(responseMsg.getRequestID());
            return;
        }
        //恢复response
        RsfResponse response = null;
        try {
            response = RuntimeUtils.recoverResponse(responseMsg, rsfFuture.getRequest(), rsfClient.getRsfContext());
            if (resStatus == ProtocolStatus.OK) {
                rsfClient.putResponse(responseMsg.getRequestID(), response);
            } else {
                String errorMessage = (String) response.getResponseData();
                rsfClient.putResponse(responseMsg.getRequestID(), new RsfException(resStatus, errorMessage));
            }
        } catch (Throwable e) {
            rsfClient.putResponse(responseMsg.getRequestID(), e);
            return;
        }
    }
}