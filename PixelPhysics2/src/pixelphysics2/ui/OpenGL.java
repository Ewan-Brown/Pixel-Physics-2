package pixelphysics2.ui;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static pixelphysics2.main.Inputerface.lastLeftClickPress;
import static pixelphysics2.main.Inputerface.lastLeftClickTime;

import java.awt.Point;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import pixelphysics2.main.Data;
import pixelphysics2.main.Drawing;
import pixelphysics2.main.Inputerface;
import pixelphysics2.main.Main;
import pixelphysics2.main.Particle;

public class OpenGL implements Runnable {
	static private long window;
	public static final int WIDTH = 1920;
	public static final int HEIGHT = 1080;
	public static boolean mouseStatus;

	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");
		init();
		loop();
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
		System.exit(0);
	}

	protected static GLFWMouseButtonCallback mouseCallback;

	private void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are
		// already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden
		// after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be
		// resizable

		// Create the window
		window = glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			Inputerface.keySet[key] = action != GLFW.GLFW_RELEASE;
			if (key == org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE && action == org.lwjgl.glfw.GLFW.GLFW_RELEASE)
				org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose(window, true);
		});
		// RMB == 1
		// LMB == 0
		// MMB == 2???
		GLFW.glfwSetMouseButtonCallback(window,
				mouseCallback = GLFWMouseButtonCallback.create((window, button, action, mods) -> {
					boolean flag = (action == 0) ? false : true;
					switch (button) {
					case 0:
						if(flag){
							Inputerface.lastLeftClickTime = System.nanoTime();
							Inputerface.lastLeftClickPress = new Point((int)getCursorPos()[0],(int)getCursorPos()[1]);
						}
						else{
							Main.flick((int)lastLeftClickPress.getX(),(int)lastLeftClickPress.getY(),(int)getCursorPos()[0],(int)getCursorPos()[1],System.nanoTime() - lastLeftClickTime);;
							Inputerface.lastLeftClickTime = null;
							Inputerface.lastLeftClickPress = null;
							
						}
						break;
					case 1:
						Inputerface.RMB = flag;
						break;
					case 2:
						break;
					}
				}));

		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);
			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			glfwSetWindowPos(window, 0, 30);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);
		// Make the window visible
		glfwShowWindow(window);
	}

	public static double[] getCursorPos() {
		try {
			DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
			DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
			GLFW.glfwGetCursorPos(window, xBuffer, yBuffer);
			double x = xBuffer.get(0);
			double y = yBuffer.get(0);
			return new double[] { x, y };
		} catch (java.lang.NullPointerException e) {
			return new double[] { 0, 0 };
		}
	}

	private void loop() {
		// bindings available for use.
		GL.createCapabilities();
		// Set the clear color
		glClearColor(0, 0f, 0f, 0.0f);
		
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		GL11.glScalef(1f / WIDTH, 1f / HEIGHT, 1);
		GL11.glTranslatef(-WIDTH, 0, 0);
		while (!glfwWindowShouldClose(window)) {
			if (Data.fill) {
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			}
			else{
				GL11.glDisable(GL11.GL_BLEND);
//				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			}
			if (!Data.paint) {
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			}
			Data.lastMouse = Inputerface.getMouseLocation();
			switch (Data.s) {
			case DOT:
				GL11.glBegin(GL11.GL_POINTS);
				for (int i = 0; i < Data.particleNum; i++) {
					if (Data.particles[i] == null)
						continue;
					int RGB = Drawing.getRGB(Data.particles[i]);
					GL11.glColor4f((RGB >> 16 & 0xFF) / 255f, (RGB >> 8 & 0xFF) / 255f, (RGB & 0xFF) / 255f,
							Data.particles[i].alpha);
					GL11.glVertex2f((float) Data.particles[i].x * 2, HEIGHT - (float) Data.particles[i].y * 2);
				}
				break;
			case LINE:
				GL11.glBegin(GL11.GL_LINES);
				for (int i = 0; i < Data.particleNum; i++) {
					if (Data.particles[i] == null)
						continue;
					int RGB = Drawing.getRGB(Data.particles[i]);
					GL11.glColor4f((RGB >> 16 & 0xFF) / 255f, (RGB >> 8 & 0xFF) / 255f, (RGB & 0xFF) / 255f,
							Data.particles[i].alpha);
					GL11.glVertex2f((float) Data.particles[i].x * 2, HEIGHT - (float) Data.particles[i].y * 2);
					GL11.glVertex2f((float) Data.particles[i].lastX * 2, HEIGHT - (float) Data.particles[i].lastY * 2);
				}
				break;
			case TRIANGLES:
				GL11.glBegin(GL11.GL_TRIANGLES);
				for (int i = 0; i < Data.particleNum; i++) {
					if (Data.particles[i] == null)
						continue;
					int RGB = Drawing.getRGB(Data.particles[i]);
					GL11.glColor4f((RGB >> 16 & 0xFF) / 255f, (RGB >> 8 & 0xFF) / 255f, (RGB & 0xFF) / 255f,
							Data.particles[i].alpha);
					GL11.glVertex2f((float) Data.particles[i].x * 2, HEIGHT - (float) Data.particles[i].y * 2);
				}
				break;
			case TAIL:
				GL11.glBegin(GL11.GL_QUADS);
				for (int i = 0; i < Data.particleNum; i++) {
					if (Data.particles[i] == null)
						continue;
					int RGB = Drawing.getRGB(Data.particles[i]);
					GL11.glColor4f((RGB >> 16 & 0xFF) / 255f, (RGB >> 8 & 0xFF) / 255f, (RGB & 0xFF) / 255f,
							Data.particles[i].alpha);
					float[] b = getPoly(Data.particles[i]);
					for (int j = 0; j < 4; j++) {
						GL11.glVertex2f((float) b[j * 2] * 2, HEIGHT - (float) b[j * 2 + 1] * 2);
					}
				}
				break;
			case CONNECTMOUSE:
				GL11.glBegin(GL11.GL_LINES);
				double[] cursorCoords = getCursorPos();
				for (int i = 0; i < Data.particleNum; i++) {
					if (Data.particles[i] == null)
						continue;
					int RGB = Drawing.getRGB(Data.particles[i]);
					GL11.glColor4f((RGB >> 16 & 0xFF) / 255f, (RGB >> 8 & 0xFF) / 255f, (RGB & 0xFF) / 255f,
							Data.particles[i].alpha);
					GL11.glVertex2f((float) Data.particles[i].x * 2, HEIGHT - (float) Data.particles[i].y * 2);
					GL11.glVertex2f((float) cursorCoords[0] * 2, HEIGHT - (float) cursorCoords[1] * 2);
				}
				break;
			default:
				GL11.glBegin(GL11.GL_POINTS);
				for (int i = 0; i < Data.particleNum; i++) {
					if (Data.particles[i] == null)
						continue;
					int RGB = Drawing.getRGB(Data.particles[i]);
					GL11.glColor4f((RGB >> 16 & 0xFF) / 255f, (RGB >> 8 & 0xFF) / 255f, (RGB & 0xFF) / 255f,
							Data.particles[i].alpha);
					GL11.glVertex2f((float) Data.particles[i].x * 2, HEIGHT - (float) Data.particles[i].y * 2);
				}
				break;
			}

			GL11.glEnd();
			glfwSwapBuffers(window); // swap the color buffers
			glfwPollEvents();
		}
	}

	public static float[] getPoly(Particle p) {
		double x = p.x, y = p.y, lastX = p.lastX, lastY = p.lastY, s = 6;
		double m = (lastY - y) / (lastX - x);
		double m2 = -1 / m;
		double a = Math.atan(m2);
		float[] b = new float[8];
		double dX = Math.cos(a) * (double) s / 2;
		double dY = Math.sin(a) * (double) s / 2;
		b[0] = (int) (x + dX);
		b[1] = (int) (y + dY);
		b[2] = (int) (lastX + dX);
		b[3] = (int) (lastY + dY);
		b[4] = (int) (lastX - dX);
		b[5] = (int) (lastY - dY);
		b[6] = (int) (x - dX);
		b[7] = (int) (y - dY);
		return b;
	}

}
