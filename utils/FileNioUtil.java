
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author penghcn
 * @created  2014-10-14
 */
public class FileNioUtil {
	public static void FileCopy(String fs,String fd) throws Exception {
		FileCopyNio(new File(fs),new File(fd));
	}
	
	public static void FileDelEmpty(String root) throws Exception {
		List<File> ls = visitAll(new File(root));
		removeNullFile(ls);
	}
	
	// 得到某一目录下的所有文件夹
	private static List<File> visitAll(File root)
    {
        File[] dirs = root.listFiles();
        List<File> list = new ArrayList<File>();
        if (dirs != null)
        {
            for (int i = 0; i < dirs.length; i++)
            {
                if (dirs[i].isDirectory())
                {
                    System.out.println("name:" + dirs[i].getPath());
                    list.add(dirs[i]);
                }
                visitAll(dirs[i]);
            }
        }
        return list;
    }
    /**
     * 删除空的文件夹
     * @param list
     */
    private static void removeNullFile(List<File> list)
    {
        for (int i = 0; i < list.size(); i++)
        {
            File temp = list.get(i);
            // 是目录且为空
            if (temp.isDirectory() && temp.listFiles().length <= 0)
            {
                temp.delete();
            }
        }
    }
	
	/**
	 * 复制文件
	 * @param fs
	 * @param fd
	 */
	public static void FileCopyNio(File fs,File fd) throws Exception {
		long t = System.currentTimeMillis();
		if (!fd.getParentFile().exists()) {
			LogWriter.info("create ParentFile: "+fd.getAbsolutePath());
			fd.getParentFile().mkdirs();
		}
			
		
		FileInputStream fin = new FileInputStream(fs);
		FileOutputStream fout = new FileOutputStream(fd);
		
		int length=2097152;//2m内存
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
		
		LogWriter.info("CopyNio() spend time: "+(System.currentTimeMillis() - t)+" ms");
	}

	/**
	 * 
	 * @param path
	 */
	public static void dirDelte(String path) {
		//
		File dirFile  = null ;
		try {
			dirFile = new File(path);
			
			if ((dirFile.exists() && dirFile.isDirectory())) {	
				File files[] = dirFile.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory())
						dirDelte(files[i].getAbsolutePath());
					else
						files[i].delete();
				}
				
			}
			dirFile.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		LogWriter.info("递归删除文件夹："+ path);
	}
}
