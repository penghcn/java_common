package cn.pengh.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import cn.pengh.io.UnicodeReader;
import cn.pengh.library.Log;

/**
 * @author penghcn
 * @created 2014年11月14日下午1:53:51
 */
public class FileUtil {
	public static final short BUFFER_LENGTH = 1024;
	public static final String CHARSET_DEFAULT = "utf-8";
	private final static double S_B = 1024d,S_KB = 1048576d,S_MB = 1073741824d,S_GB = 1099511627776d,S_TB = 1125899906842624d,S_PB = 1152921504606846976d;
	
	// 关键代码 执行序列化和反序列化 进行深度拷贝
	public static <T> List<T> deepCopy(List<T> src)  {
		ByteArrayOutputStream byteOut = null;
		ObjectOutputStream out = null;
		try {
			byteOut = new ByteArrayOutputStream();
	        out = new ObjectOutputStream(byteOut);
	        out.writeObject(src);
	 
	        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
	        ObjectInputStream in = new ObjectInputStream(byteIn);
	        @SuppressWarnings("unchecked")
	        List<T> dest = (List<T>) in.readObject();
	        return dest;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (byteOut != null){
				try {
					byteOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
        
    }
	
	public static String GetFileContents(String filePath){
		BufferedReader scan=null;
		try{
			StringBuilder sb=new StringBuilder();
			//scan=new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			scan=new BufferedReader(new UnicodeReader(new FileInputStream(filePath),CHARSET_DEFAULT));//skip utf-8 bom
			
			while(true){
				String line=scan.readLine();
				if(line==null)
					break;
				sb.append(line);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("无法获取文件");
		}finally{
			if(scan!=null){
				try {
					scan.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}				
		}
	}
	public static void WriteContentsToFile(String filePath,String content) {  
		FileOutputStream fos = null;
		try {  
        	Log.info("WriteStringToFile: "+filePath);
        	File file = new File(filePath);
    		if (!file.getParentFile().exists())
    			file.getParentFile().mkdirs();
    		if (!file.exists())
    			file.createNewFile();
            fos = new FileOutputStream(file);   
            fos.write(content.getBytes()); 
            fos.close();  
        } catch (Exception e) {   
            e.printStackTrace();  
        }  finally {
        	if (fos != null){
        		try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
    } 
	
	public static byte[] ReadFile(String root){     
		BufferedInputStream in = null;
		ByteArrayOutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(root));        
	        out = new ByteArrayOutputStream(BUFFER_LENGTH);     
	            
	        Log.info("Available bytes: " + formatFileSize(in.available()));
	       
	        byte[] temp = new byte[BUFFER_LENGTH];        
	        int size = 0;        
	        while ((size = in.read(temp)) != -1) {        
	            out.write(temp, 0, size);        
	        }        
	        in.close();        
	       
	        byte[] content = out.toByteArray();        
	        Log.info("Readed bytes count: " + formatFileSize(content.length));  
	        return content;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (in != null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			in = null;
			out = null;
		}
        
    }
	
	public static void ModPropFile(String path,String key,String val){
		ModUniqueSingleLine(path, key, key+"="+val);
	}
	public static void ModUniqueSingleLine(String path,String lineStart,String line){     
		BufferedReader in = null;
		OutputStreamWriter out = null;
		try {
			in = new BufferedReader(new FileReader(path));                
	       
	        String str = null;
	        StringBuilder sb = new StringBuilder(); 
	        while ((str = in.readLine()) != null) {  
	        	if (str.startsWith(lineStart)) {
	        		sb.append(line)  ;
	        	} else {
	        		sb.append(str)  ;
	        	}   
        		sb.append(System.getProperty("line.separator"))  ;
	        }        
	        in.close();

	        out = new OutputStreamWriter(new FileOutputStream(path));
	        out.write(sb.toString());
	        out.close();	             
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			in = null;
			out = null;
		}
        
    }
	
	
	
	/**
	 * 递归删除文件夹
	 * @param path
	 */
	public static void DirDelte(String path) {
		File dirFile  = null ;
		try {
			dirFile = new File(path);
			
			if ((dirFile.exists() && dirFile.isDirectory())) {	
				for (File f : dirFile.listFiles()) {
					if (f.isDirectory())
						DirDelte(f.getAbsolutePath());
					else
						f.delete();
				}
				
			}
			dirFile.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.trace("Done...递归删除文件夹："+ path);
	}
	
	public static void FileDelEmpty(String root) {
		removeEmptyDirs(visitAllDirs(new File(root)));
	}
	
	//得到某一目录下的所有文件夹
	private static List<File> visitAllDirs(File root) {
        List<File> list = new ArrayList<File>();
        visitAllDirs(root, list);
        return list;
    }
	
	//递归获取
	private static void visitAllDirs(File root,List<File> list) {
		File[] dirs = root.listFiles();
        if (dirs == null)
        	return ;
        for (File dir : dirs) {
			if (dir.isDirectory())
				list.add(dir);
            visitAllDirs(dir,list);
		}
	}
  
    private static void removeEmptyDirs(List<File> dirs) {
    	for (File dir : dirs) {
			if (!(dir.isDirectory() && (dir.list() == null || dir.list().length <= 0)))
				continue;
			Log.debug("Empty Dir " + dir.getAbsolutePath());
			dir.delete();
		}
    }
    public static void FileCopy(String fs,String fd) {
		FileCopy(new File(fs),new File(fd));
	}
	/**
	 * 复制文件
	 * @param fs
	 * @param fd
	 */
    public static void FileCopy(File fs,File fd){
		try {
			FileCopy(new FileInputStream(fs), fd);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
					
    }
    public static void FileCopy(InputStream fin,String fd){
    	FileCopy(fin, new File(fd));
    }
    public static void FileCopy(InputStream fin,File fd){
    	if (!fd.getParentFile().exists()) {
			Log.debug("--create ParentFile: "+fd.getParentFile().getAbsolutePath());
			fd.getParentFile().mkdirs();
		}
    	try {
			FileCopy(fin, new FileOutputStream(fd));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}					
    }
	public static void FileCopy(InputStream fin,OutputStream fout){
		long t = System.currentTimeMillis();			
		try {			
			int length=2097152;//2M内存
			byte[] buffer=new byte[length];
			
			while(true) {
				int ins = fin.read(buffer);
				if (ins == -1) {
					fin.close();
					fout.flush();
					fout.close();
					break;
				} else 
					fout.write(buffer,0,ins);
			}		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fin != null)
					fin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (fout != null)
					fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			fin = null;
			fout = null;
		}		
		Log.debug("--CopyFile spends: "+(System.currentTimeMillis() - t)+" ms");
	}
	
	public static String formatFileSize(long fileSize){
        if (fileSize > Long.MAX_VALUE || fileSize < S_B){
            return fileSize+"B";
        } else if (fileSize < S_KB){
            return CurrencyUtil.convert(fileSize/S_B,3).toString() + "K";
        } else if (fileSize < S_MB){
            return CurrencyUtil.convert(fileSize/S_KB,3).toString() + "M";
        } else if (fileSize < S_GB){
            return CurrencyUtil.convert(fileSize/S_MB,3).toString() + "G";
        } else if (fileSize < S_TB){
            return CurrencyUtil.convert(fileSize/S_GB,3).toString() + "T";
        } else if (fileSize < S_PB){
            return CurrencyUtil.convert(fileSize/S_TB,3).toString() + "P";
        } else {
            return CurrencyUtil.convert(fileSize/S_PB,3).toString() + "E";
        }
    }
}
