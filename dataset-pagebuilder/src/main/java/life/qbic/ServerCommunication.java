package life.qbic;
import com.jcraft.jsch.*;

import java.io.File;

/**
 * Allows to transfer your local files to a webserver using SFTP
 * ssh-keyscan -t rsa fair.qbic.uni-tuebingen.de >> ~/.ssh/known_hosts --> necessary
 */
public class ServerCommunication {
   private Session workingSession;

   public static ServerCommunication authenticate(String user) throws JSchException {
      // define login parameters
      String sshDir = System.getProperty("user.home") + File.separator + ".ssh" + File.separator;
      String host = "fair.qbic.uni-tuebingen.de";
      int port = 22;

      //authenticate the session with public key authorization
      JSch jsch = new JSch();
      jsch.setKnownHosts(sshDir + "known_hosts");
      jsch.addIdentity(sshDir + "id_rsa");
      Session session = jsch.getSession(user, host, port);
      session.connect();

      ServerCommunication sc = new ServerCommunication();
      sc.setWorkingSession(session);

      System.out.println("sucessfully authenticated");

      return sc;
   }

   public void transferFile(File file, String remoteLocation) throws JSchException, SftpException {
      String remoteFile = remoteLocation + file.getName();

      // set up a channel & transfer the file to server
      ChannelSftp channel = (ChannelSftp) this.workingSession.openChannel("sftp");
      channel.connect();
      channel.put(file.toString(), remoteFile, ChannelSftp.OVERWRITE);
      channel.exit();

      System.out.println("File is transferred to the server");
   }


   public void setWorkingSession(Session workingSession) {
      this.workingSession = workingSession;
   }

   public void closeConnections() {
      this.workingSession.disconnect();
   }
}
