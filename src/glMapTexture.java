
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.glfw.GLFW.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;

public class glMapTexture extends Abs {

	private long win;
	private BufferedImage[] mapBI = getBufferedImage();
	private int[] indices = { 0, 1, 2, 3 };
	private float[] textures = { 0, 0, 0, 1, 1, 1, 1, 0 };
	private float[] vertexs;

	public glMapTexture(long window) {
		win = window;

		for (int i = 0; i < mapBI.length; i++) {

			BufferedImage bi = mapBI[i];
			Tex(bi);

		}

	}

	private void Tex(BufferedImage bi) {
		vertexs = null;

		float[] coord = getVertex(bi);
		int len = coord.length;

		for (int i = 0; i < len; i += 4) {

			for (int j = 0; j < 4; j++) {
				
				vertexs[j] = 2 * coord[j + i] - 1;
				
			}
			
		    Texture texture = new Texture(bi);
			VBO vbo = new VBO(vertexs, textures, indices);
			texture.bind();
			vbo.render();
			glfwSwapBuffers(win);
			vertexs = null;

		}

	}
}

class Texture {
	private int width;
	private int height;
	private int id;

	public Texture(BufferedImage bi) { // Image NUmbers

		width = bi.getWidth();
		height = bi.getHeight();

		int[] pixels_raw = new int[width * height * 4];
		ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);

		for (int j = height - 1; j > 0; j--) {
			for (int i = 0; i < width; i++) {
				int pixel_single = pixels_raw[j * width + i];

				pixels.put((byte) ((byte) (pixel_single >> 16) & 0xFF));/** ??? RED?? */
				pixels.put((byte) ((byte) (pixel_single >> 8) & 0xFF));/** ??? GREEN? */
				pixels.put((byte) ((byte) (pixel_single >> 0) & 0xFF));/** ??? BLUE?? */
				pixels.put((byte) ((byte) (pixel_single >> 24) & 0xFF));/** ??? ALPHA?? */

			}
		}
		pixels.flip();

		int id = glGenTextures();

		glBindTexture(GL_TEXTURE_2D, id);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
	}

	public void bind() { // bind texture
		glBindTexture(GL_TEXTURE_2D, id);
	}

	public int getID() {
		return id;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}

class VBO {
	private int v_id;
	private int tex_id;
	private int in_id;
	private int draw_count;

	public VBO(float[] vertex, float[] texture, int[] indices) {

		draw_count = indices.length;

		v_id = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, v_id);
		glBufferData(GL_ARRAY_BUFFER, glCreateFloatBuffer(vertex), GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		tex_id = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, tex_id);
		glBufferData(GL_ARRAY_BUFFER, glCreateFloatBuffer(texture), GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		in_id = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, in_id);
		IntBuffer ib = BufferUtils.createIntBuffer(indices.length);
		ib.put(indices);
		ib.flip();
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, ib, GL_STATIC_DRAW);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

	}

	private FloatBuffer glCreateFloatBuffer(float[] array) {
		FloatBuffer fb = BufferUtils.createFloatBuffer(array.length);
		fb.put(array);
		return fb;
	}

	public void render() {

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);

		glBindBuffer(GL_ARRAY_BUFFER, v_id);
		glVertexPointer(2, GL_FLOAT, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glBindBuffer(GL_ARRAY_BUFFER, tex_id);
		glTexCoordPointer(2, GL_FLOAT, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, in_id);
		glDrawElements(GL_QUADS, draw_count, GL_UNSIGNED_INT, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
	}

}

abstract class Abs {
	public BufferedImage[] getBufferedImage() {
		return null;
	};

	public float[] getVertex(BufferedImage bi) {
		return null;
	};
}
