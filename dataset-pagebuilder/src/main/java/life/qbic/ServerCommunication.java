package life.qbic;
import com.jcraft.jsch.*;

import java.io.File;

/**
 * Allows to transfer your local files to a webserver using SFTP
 * ssh-keyscan -t rsa fair.qbic.uni-tuebingen.de >> ~/.ssh/known_hosts --> necessary
 */
public class ServerCommunication {

   private static Session authenticate() throws JSchException {

      String sshDir = System.getProperty("user.home") + File.separator + ".ssh" + File.separator;
      String host = "fair.qbic.uni-tuebingen.de";
      String username = "root";
      int port = 22;

      JSch jsch = new JSch();
      jsch.setKnownHosts(sshDir + "known_hosts");
      jsch.addIdentity(sshDir + "id_rsa");
      Session session = jsch.getSession(username, host, port);
      session.connect();


      return session;
   }

   public static void transferFile(File file) throws JSchException, SftpException {
      String remoteFile = "/var/www/html/" + file.getName();
      Session workingSession = authenticate();

      // set up a channel to transfer the file through
      ChannelSftp channel = (ChannelSftp) workingSession.openChannel("sftp");
      channel.connect();
      channel.put(file.toString(), remoteFile);

      //close all connections
      channel.exit();
      workingSession.disconnect();

   }





}
