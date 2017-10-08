

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/*******************************************************************************
 * 功能说明: 从ftp服务器指定目录复制文件到本地指定路径
 * 
 * @author qianshu
 * @time 2012-6-25
 ******************************************************************************/
public class FtpDownFiles {

	private FTPClient ftpClient;

	public FtpDownFiles() {
		this.ftpClient = new FTPClient();
	}

	/**
	 * 功能说明：通过递归实现ftp目录文件与本地文件同步更新
	 * 
	 * @param ftpfilepath
	 *            当前ftp目录
	 * @param localpath
	 *            当前本地目录
	 */
	public void ftpDownFiles(String ftpfilepath, String localpath) {

		try {

			//System.out.println(ftpfilepath);
			FTPFile[] ff = ftpClient.listFiles(ftpfilepath);
			// 得到当前ftp目录下的文件列表

			if (ff != null) {
				for (int i = 0; i < ff.length; i++) {
					//System.out.println(ff[i].getName());
					String localfilepath = localpath + ff[i].getName();
					File localFile = new File(localfilepath);
					// 根据ftp文件生成相应本地文件
					Date fflastModifiedDate = ff[i].getTimestamp().getTime();
					// 获取ftp文件最后修改时间
					Date localLastModifiedDate = new Date(localFile
							.lastModified());
					// 获取本地文件的最后修改时间
					int result = localLastModifiedDate
							.compareTo(fflastModifiedDate);
					// result=0，两文件最后修改时间相同；result<0，本地文件的最后修改时间早于ftp文件最后修改时间；result>0，则相反
					if (ff[i].isDirectory()) {
						// 如果是目录
						localFile.mkdir(); 
						// 如果本地文件夹不存在就创建
						String ftpfp = ftpfilepath + ff[i].getName() + "/";
						// 转到ftp文件夹目录下
						String localfp = localfilepath + "/";
						// 转到本地文件夹目录下
						this.ftpDownFiles(ftpfp, localfp);
						// 递归调用

					}
					if (ff[i].isFile()) {
						// 如果是文件
						File lFile = new File(localpath);
						lFile.mkdir();
						// 如果文件所在的文件夹不存在就创建
						if (!lFile.exists()) {
							return;
						}
						if (ff[i].getSize() != localFile.length() || result < 0) {
							// 如果ftp文件和本地文件大小不一样或者本地文件不存在或者ftp文件有更新，就进行创建、覆盖
							String filepath = ftpfilepath + ff[i].getName();
							// 目标ftp文件下载路径
							FileOutputStream fos = new FileOutputStream(
									localFile);
							boolean boo;
							try {
								boo = ftpClient.retrieveFile(new String(
										filepath.getBytes("UTF-8"),
										"ISO-8859-1"), fos);
								// 从FTP服务器上取回一个文件
							} catch (Exception e) {
								boo = false;
								e.printStackTrace();
							}

							if (boo == true) {
                                String name=ff[i].getName();
                                String dir=localpath;                               
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String time=sdf.format(localFile.lastModified());
//                                Start test=new Start();
//                                test.getConn(name, dir, time);
                                System.out.println(name+"   "+dir+"  "+time);
							} else {

							}

							fos.flush();
							// 将缓冲区中的数据全部写出
							fos.close();
							// 关闭流
						} else {
						//	System.out.println("两个文件相同！");
						}
					}

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}
	

	/**
	 * 功能说明：连接ftp服务器
	 * 
	 * @param hostip
	 *            服务器地址
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 */
	public void connectServer(String hostip, String username, String password) {

		try {
			ftpClient.connect(hostip, 21);
			// 连接服务器
			ftpClient.login(username, password);
			// 登录
			// 检测是否连接成功
			int reply = ftpClient.getReplyCode();
			// 看返回的值是不是230，如果是，表示登陆成功
			if (!FTPReply.isPositiveCompletion(reply)) {
				// 返回的code>=200&&code<300return
				ftpClient.disconnect();
				// 关闭FTP连接
			}
			ftpClient.setControlEncoding("UTF-8");
			// 设置字符编码
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			// 设置文件传输格式

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	/**
	 * 功能说明：根据ftp文件30秒刷新一次本地文件
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

//		while (true) {
			try {

				FtpDownFiles fd = new FtpDownFiles();
				fd.connectServer("192.168.0.5", "ossuser", "Changeme_123");
				fd.ftpDownFiles("/tool/Colors", System.getProperty("user.dir") + "\\localdir\\");

			} catch (Exception e) {
				System.out.println("读取异常");
			}

	}

}
