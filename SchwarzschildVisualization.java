import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class SchwarzschildVisualization {

    private long window;

    public void run() {
        System.out.println("Starting Schwarzschild Visualization...");
        init();
        loop();

        // Free memory
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        // Create the window
        window = glfwCreateWindow(800, 600, "Schwarzschild Spacetime Visualization", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Set up OpenGL context
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);  // Enable v-sync
        glfwShowWindow(window);
    }

    private void loop() {
        // Initialize OpenGL capabilities
        GL.createCapabilities();

        // Set background color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // Enable depth testing
        glEnable(GL_DEPTH_TEST);

        // Main render loop
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // Render the Schwarzschild curvature
            renderSchwarzschild();

            // Swap buffers and poll events
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private void renderSchwarzschild() {
        int gridSize = 50;  // Number of points in the grid
        float gridSpacing = 0.1f;  // Distance between points
        float mass = 1.0f;  // Mass of the black hole (scaled for visualization)

        // Schwarzschild radius (scaled to avoid extreme values)
        float schwarzschildRadius = 2.0f;

        glPushMatrix();
        glTranslatef(0.0f, -1.0f, -5.0f);  // Move the grid into view
        glRotatef(30, 1.0f, 0.0f, 0.0f);  // Rotate for better visualization

        glBegin(GL_LINES);
        glColor3f(0.3f, 0.6f, 1.0f);  // Grid color

        // Loop through grid points and deform based on Schwarzschild metric
        for (int x = -gridSize; x < gridSize; x++) {
            for (int z = -gridSize; z < gridSize; z++) {
                float r = (float) Math.sqrt(x * x + z * z) * gridSpacing;  // Radial distance
                float y = -schwarzschildRadius / r;  // Deformation (scaled)

                // Draw grid lines
                glVertex3f(x * gridSpacing, y, z * gridSpacing);
                glVertex3f((x + 1) * gridSpacing, y, z * gridSpacing);

                glVertex3f(x * gridSpacing, y, z * gridSpacing);
                glVertex3f(x * gridSpacing, y, (z + 1) * gridSpacing);
            }
        }

        glEnd();
        glPopMatrix();
    }

    public static void main(String[] args) {
        new SchwarzschildVisualization().run();
    }
}