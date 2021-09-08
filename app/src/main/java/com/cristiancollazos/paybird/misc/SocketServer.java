package com.cristiancollazos.paybird.misc;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

import com.cristiancollazos.paybird.misc.enums.ErrorConstants;
import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.LocalRepository;
import com.cristiancollazos.paybird.repository.dto.SettingDTO;
import com.cristiancollazos.paybird.repository.impl.LocalRepositoryImpl;

public class SocketServer {
    public static final String TAG = SocketServer.class.getSimpleName();

    private OutputStream out = null;
    private BufferedReader br = null;
    private InputStream in = null;
    private DataInputStream dis = null;
    private Socket objSocket;
    private int nuTimeOut = 30000;
    private String sbIp;
    private int nuPort;

    public SocketServer() {
        try {
            LocalRepository objLocalRepository = new LocalRepositoryImpl();
            SettingDTO objNetworkSettings = objLocalRepository
                    .getSingleData(Constants.LOCALKEY_SETTINGS, SettingDTO.class);

            if (objNetworkSettings != null) {
                sbIp = objNetworkSettings.getSbIp();
                nuPort = Integer.valueOf(objNetworkSettings.getSbPort());
            } else {
                sbIp = Constants.DEFAULT_IP;
                nuPort = Integer.valueOf(Constants.DEFAULT_PORT);
            }
        } catch (Exception ex) {
        }
    }

    public String send(String sbMessage) throws AppException {
        try {
            Log.d(TAG, "Enviando->" + sbMessage);

            String sbMessageServer = "";

            objSocket = new Socket();
            objSocket.connect(new InetSocketAddress(sbIp, nuPort), nuTimeOut);

            out = objSocket.getOutputStream();
            sbMessage = sbMessage + "\n";
            out.write(sbMessage.getBytes());
            out.flush();

            br = new BufferedReader(new InputStreamReader(objSocket.getInputStream(), "UTF-8"));
            String readLine = "";
            readLine = br.readLine();
            sbMessageServer = readLine;

            out.close();
            if (in != null) {
                in.close();
            }
            if (br != null) {
                br.close();
            }
            if (dis != null) {
                dis.close();
            }
            if (objSocket != null)
                objSocket.close();

            Log.d(TAG, "Server Msg->" + sbMessageServer);
            return sbMessageServer;
        } catch (UnknownHostException ex) {
            throw new AppException(ErrorConstants.E0001);
        } catch (IOException ex) {
            throw new AppException(ErrorConstants.E0002);
        } catch (Exception e) {
            throw new AppException(-1, e.getMessage());
        }
    }

}
