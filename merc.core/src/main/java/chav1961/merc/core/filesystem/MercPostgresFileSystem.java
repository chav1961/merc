package chav1961.merc.core.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;
import java.util.regex.Pattern;

import chav1961.purelib.basic.exceptions.EnvironmentException;
import chav1961.purelib.fsys.AbstractFileSystem;
import chav1961.purelib.fsys.interfaces.DataWrapperInterface;
import chav1961.purelib.fsys.interfaces.FileSystemInterface;

public class MercPostgresFileSystem extends AbstractFileSystem {
	private static final String	POSTGRES_URI = "merc_postgres";
	
	@Override
	public boolean canServe(final URI uriSchema) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public FileSystemInterface newInstance(URI uriSchema) throws EnvironmentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileSystemInterface clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataWrapperInterface createDataWrapper(URI actualPath) throws IOException {
		return new PostgresDataWrapper(actualPath);
	}

	private class PostgresDataWrapper implements DataWrapperInterface {
		private PostgresDataWrapper(final URI actualPath) {
			
		}
		
		@Override
		public URI[] list(Pattern pattern) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void mkDir() throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void create() throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setName(String name) throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void delete() throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public OutputStream getOutputStream(boolean append) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public InputStream getInputStream() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Map<String, Object> getAttributes() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void linkAttributes(Map<String, Object> attributes) throws IOException {
			// TODO Auto-generated method stub
			
		}
		
	}
}
