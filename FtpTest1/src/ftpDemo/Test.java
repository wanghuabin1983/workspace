package ftpDemo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Test {
	public static void main(String[] args) {
		//����ftp
		KmConfig km = new KmConfig();
		km.setFtpHost("192.168.0.5");
		km.setFtpPort(21);
		km.setFtpUser("ossuser");
		km.setFtpPassword("Changeme_123");
		km.setFtpPath("/tool/Colors");
		
		//�ϴ���ftp
		ftpUtil util=new ftpUtil();
		File file = new File("F:/����2.jpg");
		InputStream in;
		try {
			in = new FileInputStream(file);
			util.upLoadFileFtp(km, in, "���ĺ������.jpg");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try{
			OutputStream os = new FileOutputStream("c:/1.exe");
			util.downloadFileFtp(km, "Colors_New.exe", "Colors_New.exe", os);
		}
		 catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//ҳ�����: ftp://�˻�:����@ip��ַ:�˿ں�/�ļ���Ŀ¼/
		//  ftp://test:111111@222.22.22.11:21/image/
	}
}
