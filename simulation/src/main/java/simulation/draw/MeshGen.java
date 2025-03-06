
package simulation.draw;

import java.nio.FloatBuffer;
import java.nio.IntBuffer; 
import java.util.ArrayList; 


import org.lwjgl.BufferUtils; 
import static org.lwjgl.opengl.GL46.*;

public  class MeshGen{
    private static ArrayList<Integer> vaos = new ArrayList<>();
    private static ArrayList<Integer> vbos = new ArrayList<>();

    private static FloatBuffer createFloatBuffer(float[]data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private static IntBuffer createIntBuffer(int[]data){
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private static void storeData(int attribute, int dimensions,float[]data){
        FloatBuffer buffer = createFloatBuffer(data);
        int vbo = glGenBuffers();
        vbos.add(vbo);
        glBindBuffer(GL_ARRAY_BUFFER,vbo);
        glBufferData(GL_ARRAY_BUFFER,buffer,GL_STATIC_DRAW);
        glVertexAttribPointer(attribute,dimensions,GL_FLOAT,false,0,0);
        glBindBuffer(GL_ARRAY_BUFFER,0);
    }

    private static void bindIndices(int[]data){
        IntBuffer buffer = createIntBuffer(data);
        int vbo = glGenBuffers();
        vbos.add(vbo);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,vbo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,buffer,GL_STATIC_DRAW);
    }

    public static Mesh createMesh(float[]data,int[]indices){
        int vao = genVAO();
        storeData(0,3,data);
        bindIndices(indices);
        glBindVertexArray(0);
        return new Mesh(vao,indices.length);
    }

    private static int genVAO(){
        int vao = glGenVertexArrays();
        vaos.add(vao);
        glBindVertexArray(vao);
        return vao;
    }

}