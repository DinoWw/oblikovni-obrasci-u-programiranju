#include <iostream>
#include <assert.h>
#include <stdlib.h>

struct Point{
	int x; int y;
};

class Shape {
	public: 
	Point center_;

	virtual void draw()=0;
	virtual void move(int dx, int dy)=0;
};
class Circle : public Shape { 
	public:
	double radius_;

	void draw() {
		printf("Circle x, y, r: %d, %d, %f \n", center_.x, center_.y, radius_);
	}
	void move(int dx, int dy) {
		center_.x += dx;
		center_.y += dy;
	}
};
class Square : public Shape {
	public:
	double side_;
	void draw() {
		printf("Square x, y, w: %d, %d, %f \n", center_.x, center_.y, side_);
	}
	void move(int dx, int dy) {
		center_.x += dx;
		center_.y += dy;
	}
};
class Rhomb : public Shape {
	public: 
	double dx;	// x diagonal
	double dy;	// y diagonal
	void draw() {
		printf("Rhomb x, y, dx, dy: %d, %d, %f, %f \n", center_.x, center_.y, dx, dy);
	}
	void move(int dx, int dy) {
		center_.x += dx;
		center_.y += dy;
	}
	
};
void moveShapes(Shape** shapes, int n, int dx, int dy){
	for (int i=0; i<n; ++i){
		shapes[i]->move(dx, dy);
	}
}

void drawShapes(Shape** shapes, int n){
	for (int i=0; i<n; ++i){
		shapes[i]->draw();
	}
}
int main(){
	Shape* shapes[5];
	shapes[0]=(Shape*)new Circle;
	shapes[1]=(Shape*)new Square;
	shapes[2]=(Shape*)new Square;
	shapes[3]=(Shape*)new Circle;
	shapes[4]=(Shape*)new Rhomb;

	drawShapes(shapes, 5);
	moveShapes(shapes, 5, 10, 20);
	drawShapes(shapes, 5);

}
