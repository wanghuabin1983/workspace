

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import sun.net.TelnetInputStream;
import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpProtocolException;

public class FtpApp {

	// ��Ҫ���ص��ļ���׺
	private static String[] _ext = {};

	// ����Ҫ���ص�Ŀ¼
	private static String[] _not = {};

	// ���ش洢Ŀ¼
	private static String local_path = "";
	// Զ������Ŀ¼
	private static String path = "";

	private static String username = "";
	private static String password = "";
	private static String HOST = "";
	private static int PORT = 21;
	private static boolean cover = true;

	// �������ص��ļ��б�<ȫ·��>
	// static List<String> li_file = new ArrayList<String>();
	// �������ص�Ŀ¼<ȫ·��>
	// static List<String> li_path = new ArrayList<String>();

	private static FtpClient ftpClient = null;

	/**
	 * connectServer ����ftp������
	 * 
	 * @throws java.io.IOException
	 * @param path
	 *            �ļ��У��մ����Ŀ¼
	 * @param password
	 *            ����
	 * @param user
	 *            ��½�û�
	 * @param server
	 *            ��������ַ
	 * @throws FtpProtocolException 
	 */
	public void connectServer(String server, int port, String user, String password) throws IOException, FtpProtocolException {
		// server��FTP��������IP��ַ��user:��¼FTP���������û���
		// password����¼FTP���������û����Ŀ��path��FTP�������ϵ�·��
		ftpClient = FtpClient.create();
		// ������ַ  
        SocketAddress addr = new InetSocketAddress(server, port);  
		ftpClient.connect(addr);
		ftpClient.login(user, password.toCharArray());
		// ��2�����ϴ�������
		ftpClient.setBinaryType(); 
	}

	/**
	 * closeServer �Ͽ���ftp������������
	 */
	private void closeServer() {
		try {
			if (ftpClient != null) {
				ftpClient.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ȡ��ĳ��Ŀ¼�µ������ļ��б�
	 * @throws FtpProtocolException 
	 * 
	 * @throws IOException
	 */
	private List<String> getFileList(String path) throws FtpProtocolException {
		List<String> list = new ArrayList<String>();
		BufferedReader dis = null;
		try {
			InputStream nameList = ftpClient.nameList(path);
			if (nameList != null) {
				dis = new BufferedReader(new InputStreamReader(nameList));
				String filename = "";
				while ((filename = dis.readLine()) != null) {
					list.add(filename);
				}
				System.out.println(list.toString());
			}
		} catch (IOException e) {
		} finally {
			if (dis != null) {
				try {
					dis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	/**
	 * ��FTP���ļ�����<δʹ��>
	 */
	private boolean rename(String srcFileName, String destFileName) {
		boolean flag = false;

		try {
			ftpClient.rename(srcFileName, destFileName);
			flag = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			flag = false;
		}

		return flag;
	}

	/**
	 * download ��ftp�����ļ�������
	 * 
	 * @throws java.lang.Exception
	 * @return long
	 * @param newfilename
	 *            �������ɵ��ļ���
	 * @param filename
	 *            �������ϵ��ļ���
	 */
	private long download(String filename, String newfilename) throws Exception {
		long result = 0;
		InputStream is = null;
		FileOutputStream os = null;
		try {
			is = ftpClient.getFileStream(filename);

			java.io.File outfile = new java.io.File(newfilename);
			os = new FileOutputStream(outfile);
			byte[] bytes = new byte[1024];
			int c;
			while ((c = is.read(bytes)) != -1) {
				os.write(bytes, 0, c);
				result = result + c;
			}
			outfile = null;
		} catch (IOException e) {
			e.printStackTrace();
			result = 0;
		} finally {
			if (is != null) {
				is.close();
			}
			if (os != null) {
				os.close();
			}
		}
		return result;
	}

	/**
	 * @param args
	 * @throws FtpProtocolException 
	 */
	public static void main(String[] args) throws FtpProtocolException {
		if (args == null || args.length < 1) {
			System.out.println("��һ�������������ļ�");
			System.exit(0);
		}
		FtpApp dao = new FtpApp();
		dao.init(args[0]);
		try {

			dao.connectServer(HOST, PORT, username, password);

			dao.getList(path);
			// dao.downloads(li_file, local_path);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			dao.closeServer();
		}
	}

	private void init(String s) {
		Config dao = new Config(s);
		local_path = dao.readValue("local_path").trim();
		path = dao.readValue("path").trim();
		username = dao.readValue("username").trim();
		password = dao.readValue("password").trim();
		HOST = dao.readValue("HOST").trim();
		cover = dao.readValue("cover").trim().equals("true");

		String _ext_string = dao.readValue("_ext").trim();
		String _not_string = dao.readValue("_not").trim();
		_ext_string = _ext_string.replaceAll(" ", "");
		_not_string = _not_string.replaceAll(" ", "");

		_ext = _ext_string.split(",");
		_not = _not_string.split(",");

		// for(String s:_ext){
		// System.out.println(s);
		// }
		// for(String s:_not){
		// System.out.println(s);
		// }
	}

	private void downloadsOne(String path) {
		String local_file = null;
		local_file = local_path + path;
		createPathifNotExists(local_file); 
		try {
			if (localFileExists(local_file)) {
				if (cover) {
					deletelocalFile(local_file);
				} else {
					//�Ὠ����Ŀ¼
					return;
				}
			}
			download(path, local_file);
			System.out.println(path + " is downloaded :" + local_file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deletelocalFile(String local_file) {
		new File(local_file).deleteOnExit();
	}

	private boolean localFileExists(String local_file) {
		return new File(local_file).exists();
	}

	private void downloads(List<String> li_file2, String local_path) {
		String local_file = null;
		String remoute_file = null;
		for (int i = 0; i < li_file2.size(); i++) {
			remoute_file = li_file2.get(i);
			local_file = local_path + remoute_file;
			createPathifNotExists(local_file);
			try {
				download(remoute_file, local_file);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(remoute_file + ", is download: " + local_file);
		}

	}

	private void createPathifNotExists(String local_file) {
		local_file = local_file.replaceAll("//", "/");
		int c = local_file.lastIndexOf('/');
		String fs = local_file.substring(0, c);
		File f = new File(fs);
		if (!f.exists()) {
			f.mkdirs();
		}
		f = null;
	}

	private void getList(String path) throws FtpProtocolException {
		List<String> ls = new ArrayList<String>();
		String pwd = null;

		if (path == null || path.trim().equals("")) {
			return;
		}
		try {
			ftpClient.changeDirectory(path);
			System.out.println("cd " + path);
		} catch (Exception e) {
			for (int i = 0; i < _ext.length; i++) {
				if (isOkFile(path)) {
					// /
					downloadsOne(path);

					// li_file.add(path);
					break;
				}
			}
			return;
		}
		try {
			pwd = path;
			ls = this.getFileList(pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (ls == null || ls.size() < 1) {
			return;
		}
		if (notDownloadDir(pwd)) {
			return;
		}

		// li_path.add(path);
		if (ls != null) {
			for (int i = 0; i < ls.size(); i++) {
				getList(ls.get(i));
			}
		}
	}

	private boolean notDownloadDir(String pwd) {
		for (int i = 0; i < _not.length; i++) {
			if (pwd.startsWith(_not[i])) {
				return true;
			}
		}
		return false;
	}

	private static boolean isOkFile(String path) {
		for (int i = 0; i < _ext.length; i++) {
			if (path.endsWith(_ext[i])) {
				return true;
			}
		}
		return false;
	}

}
