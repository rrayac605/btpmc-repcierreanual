package mx.gob.imss.cit.pmc.cierreanual.service.impl;

import mx.gob.imss.cit.pmc.cierreanual.commons.dto.SftpParamsDTO;
import mx.gob.imss.cit.pmc.cierreanual.service.FtpClientService;
import mx.gob.imss.cit.pmc.cierreanual.exception.DownloadException;
import mx.gob.imss.cit.pmc.cierreanual.service.SftpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class FtpServiceImpl implements FtpClientService {
    private final static Logger logger = LoggerFactory.getLogger(FtpServiceImpl.class);

    @Value("${sftpHost}")
    private String sftpHost;

    @Value("${sftpPort}")
    private String sftpPort;

    @Value("${sftpUser}")
    private String sftpUser;

    @Value("${sftpPassword}")
    private String sftpPassword;

    private SftpClient client;
//    private SftpClient getClient(){
//
//        if(client == null){
//
//            client = new SftpClient();
//            SftpParamsDTO sftpParamsDTO = new SftpParamsDTO();
//
//            sftpParamsDTO.setSftpHost(sftpHost);
//            sftpParamsDTO.setSftpPort(Integer.parseInt(sftpPort));
//            sftpParamsDTO.setSftpPasword(sftpPassword);
//            sftpParamsDTO.setSftpUser(sftpUser);
//            client.connect(sftpParamsDTO);
//            return client;
//        }else{
//
//        }
//
//
//    }

    @Override
    public synchronized void uploadFile(String source, String destinationFile) {
        Path sourcepath = null;
        Path destinationepath = null;
        SftpClient client = null;
        boolean exito = false;
        try {
            client = new SftpClient();
            SftpParamsDTO sftpParamsDTO = new SftpParamsDTO();

            sftpParamsDTO.setSftpHost(sftpHost);
            sftpParamsDTO.setSftpPort(Integer.parseInt(sftpPort));
            sftpParamsDTO.setSftpPasword(sftpPassword);
            sftpParamsDTO.setSftpUser(sftpUser);
            client.connect(sftpParamsDTO);
            client.uploadFile(source, destinationFile);
            client.disconnect();

        }
        catch (DownloadException ex){

        }

    }
}
