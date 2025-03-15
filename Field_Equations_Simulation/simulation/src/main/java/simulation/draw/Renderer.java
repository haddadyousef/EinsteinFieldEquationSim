package simulation.draw;

import java.io.*;
import java.lang.Math;
import org.joml.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class Renderer {


    public final float PI = (float)(Math.PI);
    
    public final float scale = 1737.4f;
    public final float gConstant = 0.0000000000667f  ;

    public static final float moonMass = 7.3f*(float)Math.pow(10f,22f);
    public static final float earthMass = 5.972f*(float)Math.pow(10f,24f);
   
    private Window window;
    private MouseInput mInput;

    public Renderer(){
        window = new Window();
        window.init();
        glfwShowWindow(window.getWindow());
        mInput = new MouseInput();
        mInput.init(window);
    }
    
    public void render(){
        
        
        


        glfwSwapInterval(1);
        glClearColor(0,0,0,0);
       
        Camera camera = new Camera();
        
        Object sphere1 = new Object(moonMass);
        sphere1.createSphere(50,50, 1f);
        sphere1.setPosition(new Vector3f(0f,0f,-251.25f));
        sphere1.setVelocity(new Vector3f(3f,0f,-0.3f));

        Object sphere2 = new Object(earthMass);
        sphere2.createSphere(50,50, 4f);
        sphere2.setPosition(new Vector3f(-15f,4f,-30f));
        sphere2.setVelocity(new Vector3f(0f,0f,0f));

        Object[]spheres = {sphere1, sphere2};

        Grid grid = new Grid();
        grid.createGrid(300,450);
      

       
        ShaderProgram shader = new ShaderProgram();
        shader.createVertexShader(loadResources("C:\\Users\\rohan\\OneDrive\\Programming Projects\\Field_Equations_Simulation\\simulation\\src\\main\\resources\\shader.vs"));
        shader.createFragmentShader(loadResources("C:\\Users\\rohan\\OneDrive\\Programming Projects\\Field_Equations_Simulation\\simulation\\src\\main\\resources\\fragment.fs"));
        shader.link();

        float aspectRatio = (float)window.getWidth()/window.getHeight();
        Transformations transformer = new Transformations();
        Matrix4f projectionMatrix = transformer.
        createProjectionMatrix(60f,aspectRatio,0.01f, 1000f);
        Matrix4f viewMatrix = transformer.createViewMatrix(camera);
        
        Matrix4f worldMatrix = transformer.getModelViewMatrix(sphere1,viewMatrix);

        shader.createUniform("projectionMatrix");
        shader.createUniform("worldMatrix");
        shader.bind();
        shader.setUniform("projectionMatrix", projectionMatrix);
        
        
        
        while(!glfwWindowShouldClose(window.getWindow())){

            glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
            mInput.input(window);
            if(mInput.isRightPressed()){
                Vector2f rotVector = mInput.getDisplayVec();
                camera.moveRotation(rotVector.x*0.3f,rotVector.y*0.3f,0);
            }

            Vector3f offset = window.getCameraInc();
            camera.movePosition(offset.x, offset.y, offset.z);
           
            aspectRatio = (float)window.getWidth()/window.getHeight();
           
            projectionMatrix = transformer.
            createProjectionMatrix(60f,aspectRatio,0.01f, 1000f);
            shader.setUniform("projectionMatrix", projectionMatrix);
           
            viewMatrix = transformer.createViewMatrix(camera);


            for(Object sphere:spheres){
                Vector2f totalForce = new Vector2f(0f,0f);
                for(Object nextSphere:spheres){
                    if(nextSphere!=sphere){
                        Vector3f spherePosition = sphere.getPosition();
                        Vector3f nextSpherePosition = nextSphere.getPosition();

                        float distanceZ = spherePosition.z-nextSpherePosition.z;
                        float distanceX = spherePosition.x-nextSpherePosition.x;
                        float totalDistance = (float)Math.sqrt(Math.pow(scale*distanceX,2)+
                        Math.pow(scale*distanceZ,2));

                        float gForce = gConstant*sphere.mass()*
                        nextSphere.mass()/
                        ((float)
                        Math.pow(totalDistance,2));

                        float theta = (float)Math.atan(distanceZ/distanceX);

                        if(distanceX>0&&distanceZ>0){
                            theta+=PI;
                        }
                        if(distanceZ<0&&distanceX>0){
                            theta-=PI;
                        }
                        if(distanceZ>0&&distanceX==0){
                            theta = -1*PI/2;
                        }
                        if(distanceZ<0&&distanceX==0){
                            theta = PI/2;
                        }
                        if(distanceX>0&&distanceZ==0){
                            theta = PI;
                        }
                        if(distanceX<0&&distanceZ==0){
                            theta = 0;
                        }
           
        

                        Vector2f sphereForce;
                       

                        sphereForce = new Vector2f((float)Math.cos(theta)*gForce,(float)Math.sin(theta)*gForce);
                        totalForce.x+=sphereForce.x;
                        totalForce.y+=sphereForce.y;
                    }  
                }
                sphere.setAcceleration(new Vector3f(totalForce.x/(scale*30f*sphere.mass()),0f,totalForce.y/(scale*30f*sphere.mass())));
            }   

            for(Object spher: spheres){
                spher.move();
                Vector3f sphere1Position = sphere1.getPosition();
                Vector3f position = sphere2.getPosition();

                float distanceZ = sphere1Position.z-position.z;
                float distanceX = sphere1Position.x-position.x;
               
                float totalDistance = (float)Math.sqrt(Math.pow(scale*distanceX,2)+
                Math.pow(scale*distanceZ,2));

                float rS = 0.00886f;

                sphere1.setPosition(new Vector3f(sphere1Position.x,2f*(float)Math.sqrt(rS*(totalDistance-rS)),sphere1Position.z));

                worldMatrix.identity();
                worldMatrix = transformer.getModelViewMatrix(spher,viewMatrix);
           
                shader.setUniform("worldMatrix", worldMatrix);
                spher.getMesh().render();
                spher.accelerate();
                

            }

            float[]gridVertices = grid.getVertices();
            
            for(int i=1;i<gridVertices.length;i+=3){
                float x = gridVertices[i-1];
                float z = gridVertices[i+1];

                Vector3f position = sphere2.getPosition();

                float distanceZ = z-position.z;
                float distanceX = x-position.x;
               
                float totalDistance = (float)Math.sqrt(Math.pow(scale*distanceX,2)+
                Math.pow(scale*distanceZ,2));

                float rS = 0.00886f;

                gridVertices[i] = 2f*(float)Math.sqrt(rS*(totalDistance-rS));
            }

            grid.setVertices(gridVertices);


            worldMatrix.identity();
            worldMatrix = transformer.getModelViewMatrix(grid,viewMatrix);
            shader.setUniform("worldMatrix", worldMatrix);
            grid.getMesh().renderGrid();
            
            glfwSwapBuffers(window.getWindow());
            glfwPollEvents();
        }
        glfwTerminate();
    }

    private String loadResources(String filePath){
        try{
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        StringBuilder builder = new StringBuilder();
        String nextLine="";
        while((nextLine = reader.readLine())!=null){
            builder.append(nextLine).append("\n");
        }
        reader.close();
        return builder.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return"";
    }



    
    
    public void calculateNormals(){
        
    }
}
