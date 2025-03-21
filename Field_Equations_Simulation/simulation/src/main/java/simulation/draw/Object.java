package simulation.draw;

import java.lang.Math;

import org.joml.*;

public class Object{

    public final float PI = (float)(Math.PI);

    private Mesh mesh;
    private Vector3f position;
    private Vector3f rotation;
    private Vector3f velocity;
    private Vector3f acceleration;
    private float mass;
    private float radius;

    public Object(float mass){
        this.position = new Vector3f(0,0,0);
        this.rotation = new Vector3f(0,0,0);
        this.velocity = new Vector3f(0,0,0);
        this.acceleration = new Vector3f(0,0,0);
        this.mass = mass;
    }

    public void setRotation(Vector3f rotation){
        this.rotation.x=rotation.x;
        this.rotation.y=rotation.y;
        this.rotation.z=rotation.z;
    }

    public void setPosition(Vector3f position){
        this.position.x=position.x;
        this.position.y=position.y;
        this.position.z=position.z;
    }

    public Vector3f getRotation(){
        return rotation;
    }


    public Vector3f getPosition(){
        return position;
    }

    public Mesh getMesh(){
        return mesh;
    }




     public void createSphere(int numSectors, int numStacks,float radius){
        float[] vertices = new float[1000000];
        
        float[]colors = new float[1000000];
        

        int[]indices = new int[1000000];
        
        vertices[0]=0f;
        colors[0]=1f;
        vertices[1]=radius;
        colors[1]=1f;
        vertices[2]=0f;
        colors[2]=1f;

        int index=3;
        int index1=0;
       
        for(int j=0;j<numStacks;j++){
            float stackAngle = (PI/2)-((j+1)*PI/numStacks);
             for(int i=0;i<numSectors;i++){
                float sectorAngle = 2*PI*(i+1)/numSectors;

                vertices[index]= radius*(float)(Math.cos(stackAngle)*Math.cos(sectorAngle));
                colors[index]=1f;
                index++;
                vertices[index]=radius*(float)(Math.sin(stackAngle));
                colors[index]=1f;
                index++;
                vertices[index]=radius*(float)(Math.cos(stackAngle)*Math.sin(sectorAngle));
                colors[index]=1f;
                index++;
            
                if(i==0&&j==0){
                    indices[index1]=i+1;
                    index1++;
                }
                else if (j==0&&i!=0){
                    indices[index1]=i+1;
                    index1++;
                    indices[index1]=0;
                    index1++;
                    indices[index1]=i+1;
                    index1++;
                }
                else if(i==0&&j!=0){
                    indices[index1]= (numSectors*j)+(i+1);
                    index1++;
                }
                else{
                    indices[index1]= (numSectors*j)+(i+1);
                    index1++;
                    indices[index1]= (numSectors*(j-1))+(i+1);
                    index1++;
                    indices[index1]= (numSectors*(j-1))+(i+1);
                    index1++;
                    indices[index1]= (numSectors*(j-1))+i;
                    index1++;
                    indices[index1]= (numSectors*j)+i;
                    index1++;
                    indices[index1]= (numSectors*j)+(i+1);
                    index1++;
                }

            }
        if(j==0){
        indices[index1]=1;
        index1++;
        indices[index1]=0;
        index1++;
        }
        else{
            indices[index1]=numSectors*(j)+1;
            index1++;
            indices[index1]=numSectors*(j-1)+1;
            index1++;
            indices[index1]=numSectors*(j-1)+1;
            index1++;
            indices[index1]=numSectors*(j);
            index1++;
            indices[index1]=numSectors*(j+1);
            index1++;
        }
    }
    this.radius=radius;
    this.mesh = MeshGen.createMesh(vertices, colors, indices);
}

public Vector3f velocity(){
    return velocity;
}

public void setVelocity(Vector3f velocity){
    this.velocity=velocity;
}
public Vector3f acceleration(){
    return acceleration;
}

public void setAcceleration(Vector3f acceleration){
    this.acceleration=acceleration;
}

public void accelerate(){
   velocity.x+=acceleration.x;
   velocity.y+=acceleration.y;
   velocity.z+=acceleration.z;
}

public void move(){
    position.x+=velocity.x;
    position.y+=velocity.y;
    position.z+=velocity.z;
}

public float mass(){
    return mass;
}

public float radius(){
    return radius;
}

}