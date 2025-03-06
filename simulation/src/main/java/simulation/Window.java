package simulation;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import simulation.draw.*;

import java.nio.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL46.*;


public class Window {
    
    private long window;

    public void run(){
        init();
        loop();
        
        glfwTerminate();

    }

    private void init(){
        if(!glfwInit()){
            throw new IllegalStateException("Initialization Failed");
        }
        window = glfwCreateWindow(640,480,"window",0,0);
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glfwSwapInterval(1);
        glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE);
        glfwShowWindow(window);

    }

    private void loop(){
        float[] vertices = {-0.5f,-0.5f,0f,
                             0.5f, -0.5f, 0f,
                             0.5f,0.5f,0f,
                             };

        int[] indices = {0,1,2};
        glClearColor(0,0,0,0);
        while(!glfwWindowShouldClose(window)){
            glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
            Mesh mesh = MeshGen.createMesh(vertices,indices);
            glBindVertexArray(mesh.getVaoId());
            glEnableVertexAttribArray(0);
            glDrawElements(GL_TRIANGLES,mesh.getVertices(),GL_UNSIGNED_INT,0);
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public static void main(String[]args){
        new Window().run();
    }

    
}