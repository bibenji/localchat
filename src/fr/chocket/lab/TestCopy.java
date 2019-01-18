package fr.chocket.lab;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class TestCopy {
	private List<byte[]> listBytes = new ArrayList<byte[]>();
	
	public TestCopy() {
		listBytes = new ArrayList<byte[]>();
		
		try {
			byte[] bytes;
			File file = new File("F:\\fall-in-love.jpg");
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file+".jpg"));			
			
			System.out.println((int)file.length()*8);
			
			int i = 0;
			while (i <= (int)file.length()*8) { // donne les octets
				System.out.println(i);
				// on lit 1 Mo
				if ( (i + 8000000) <= ((int)file.length()*8) ) {
					System.out.println("LA");
					bytes = new byte[8000000];
					
					bis.read(bytes, 0, 8000000);
					
				} else {
					System.out.println("ICI");
					bytes = new byte[ ((int)file.length()*8-i) ];
					bis.read( bytes, 0, ((int)file.length()*8-i) );
				}
				
//				String tempStr = bytes.toString();	
//				listBytes.add(tempStr.getBytes());
				
//				String tempStr = new String(bytes, StandardCharsets.UTF_8);				
//				listBytes.add(tempStr.getBytes(StandardCharsets.UTF_8));
				
//				String tempStr = Arrays.toString(bytes);
//				listBytes.add(tempStr.getBytes());
				
				String encoded = Base64.getEncoder().encodeToString(bytes);
				byte[] decoded = Base64.getDecoder().decode(encoded);
				listBytes.add(decoded);
				
				
				
				
				i += 8000000; // 1 Mo
			}
			
			for (byte[] bufs : listBytes)
				for(byte buf : bufs)
					bos.write(buf);					
			
			bis.close();
			bos.close();
			
			
			
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
