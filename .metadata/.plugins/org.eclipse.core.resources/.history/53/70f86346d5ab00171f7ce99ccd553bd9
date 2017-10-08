package ftpDemo;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import sun.net.TelnetInputStream;
import sun.net.TelnetOutputStream;
import sun.net.ftp.FtpClient;



public class ftpUtil {
	// 上传文件至FTP通用方法
	public static void upLoadFileFtp(KmConfig kmConfig,InputStream is, String fileName){

		try {
			String ftpHost = kmConfig.getFtpHost();
			int port = kmConfig.getFtpPort();
			String userName = kmConfig.getFtpUser();
			String passWord = kmConfig.getFtpPassword();
			String path = kmConfig.getFtpPath();
			

			FtpClient ftpClient = new FtpClient(ftpHost, port);// ftpHost为FTP服务器的IP地址，port为FTP服务器的登陆端口,ftpHost为String型,port为int型。
			ftpClient.login(userName, passWord);// userName、passWord分别为FTP服务器的登陆用户名和密码
			ftpClient.binary();
			ftpClient.cd(path);// path为FTP服务器上保存上传文件的路径。

			TelnetOutputStream telnetOut = ftpClient.put(fileName);// fileName为上传的文件名
			DataOutputStream dataOut = new DataOutputStream(telnetOut);
			byte buffer[] = new byte[1024 * 1024];
			int count = 0;
			while ((count = is.read(buffer)) != -1) {
				dataOut.write(buffer, 0, count);
			}
			telnetOut.close();
			dataOut.close();
			ftpClient.closeServer();
		} catch (Exception e) {
			System.out.println("上传文件失败！请检查系统FTP设置,并确认FTP服务启动");
		}
	}

	// 删除文件至FTP通用方法
	public static void deleteFileFtp(KmConfig kmConfig,String fileName){
		try {
			String ftpHost = kmConfig.getFtpHost();
			int port = kmConfig.getFtpPort();
			String userName = kmConfig.getFtpUser();
			String passWord = kmConfig.getFtpPassword();
			String path = kmConfig.getFtpPath();

			FtpClient ftpClient = new FtpClient(ftpHost, port);// ftpHost为FTP服务器的IP地址，port为FTP服务器的登陆端口,ftpHost为String型,port为int型。
			ftpClient.login(userName, passWord);// userName、passWord分别为FTP服务器的登陆用户名和密码
			ftpClient.binary();
			ftpClient.cd(path);// path为FTP服务器上保存上传文件的路径。
			try {
				ftpClient.sendServer("dele " + fileName + "\r\n");
			} catch (Exception e) {
				System.out.println("删除文件失败！请检查系统FTP设置,并确认FTP服务启动");
			}
			ftpClient.closeServer();
		} catch (Exception e) {
			System.out.println("删除文件失败！");
		}
	}

	// 下载ftp文件
	public static void downloadFileFtp(KmConfig kmConfig,String fileName, String clientFileName, OutputStream outputStream){
		try {
			String ftpHost = kmConfig.getFtpHost();
			int port = kmConfig.getFtpPort();
			String userName = kmConfig.getFtpUser();
			String passWord = kmConfig.getFtpPassword();
			String path = kmConfig.getFtpPath();

			FtpClient ftpClient = new FtpClient(ftpHost, port);// ftpHost为FTP服务器的IP地址，port为FTP服务器的登陆端口,ftpHost为String型,port为int型。
			ftpClient.login(userName, passWord);// userName、passWord分别为FTP服务器的登陆用户名和密码
			ftpClient.binary();
			ftpClient.cd(path);// path为FTP服务器上保存上传文件的路径。
			try {
				TelnetInputStream in = ftpClient.get(fileName);
				byte[] bytes = new byte[1024];
				int cnt=0;
				while ((cnt=in.read(bytes,0,bytes.length)) != -1) {
					outputStream.write(bytes, 0, cnt);
				}
				outputStream.close();
				in.close();
			} catch (Exception e) {
				ftpClient.closeServer();
				e.printStackTrace();
			}
			ftpClient.closeServer();
		} catch (Exception e) {
			System.out.println("下载文件失败！请检查系统FTP设置,并确认FTP服务启动");
		}
	}

 	//在ftp服务器上穿件文件夹   
        public boolean createDir(String path,FtpClient ftpClient) throws Exception{  
              
           
			//进入到home文件夹下   
            ftpClient.cd("/home");  
            //创建远程文件夹    
           //远程命令包括   
           //USER    PORT    RETR    ALLO    DELE    SITE    XMKD    CDUP    FEAT<br>   
//          PASS    PASV    STOR    REST    CWD     STAT    RMD     XCUP    OPTS<br>   
//          ACCT    TYPE    APPE    RNFR    XCWD    HELP    XRMD    STOU    AUTH<br>   
//          REIN    STRU    SMNT    RNTO    LIST    NOOP    PWD     SIZE    PBSZ<br>   
//          QUIT    MODE    SYST    ABOR    NLST    MKD     XPWD    MDTM    PROT<br>   
//               在服务器上执行命令,如果用sendServer来执行远程命令(不能执行本地FTP命令)的话，所有FTP命令都要加上/r/n<br>   
//                    ftpclient.sendServer("XMKD /test/bb/r/n"); //执行服务器上的FTP命令<br>   
//                    ftpclient.readServerResponse一定要在sendServer后调用<br>   
//                    nameList("/test")获取指目录下的文件列表<br>   
//                    XMKD建立目录，当目录存在的情况下再次创建目录时报错<br>   
//                    XRMD删除目录<br>   
//                    DELE删除文件<br>   
           //通过远程命令 穿件一个files文件夹   
            ftpClient.sendServer("MKD "+ path + "\r\n");  
            //这个方法必须在 这两个方法中间调用 否则 命令不管用   
            ftpClient.binary();  
            ftpClient.readServerResponse();  
              
            return false;  
        }  



/** 
  * 检查文件夹是否存在 
  * @param dir 
  * @param ftpClient 
  * @return 
  */ 
	public boolean isDirExist(String dir, FtpClient ftpClient) { 
	 	 try { 
	   		ftpClient.cd(dir); 
	  	} catch (Exception e) { 
	
	   		return false; 
	  	} 
 	 	return true; 
	} 

}
