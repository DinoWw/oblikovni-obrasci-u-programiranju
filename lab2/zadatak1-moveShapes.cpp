#include <iostream>
#include <assert.h>
#include <stdlib.h>

struct Point{
	int x; int y;
};
struct Shape{
	enum EType {circle, square, rhomb};
	EType type_;
};
struct Circle{
	Shape::EType type_;
	double radius_;
	Point center_;
};
struct Square{
	Shape::EType type_;
	double side_;
	Point center_;
};
struct Rhomb{
	Shape::EType type_;
	double dx;	// x diagonal
	double dy;	// y diagonal
	Point center_;
};
void drawSquare(struct Square* s){
	printf("Square x, y, w: %d, %d, %f \n", s->center_.x, s->center_.y, s->side_);
}
void drawCircle(struct Circle* s){
	printf("Circle x, y, r: %d, %d, %f \n", s->center_.x, s->center_.y, s->radius_);
}
void drawRhomb(struct Rhomb* s){
	printf("Rhomb x, y, dx, dy: %d, %d, %f, %f \n", s->center_.x, s->center_.y, s->dx, s->dy);
}
void moveSquare(struct Square *s, int dx, int dy) {
	s->center_.x += dx;
	s->center_.y += dy;
}
void moveCircle(struct Circle *s, int dx, int dy) {
	s->center_.x += dx;
	s->center_.y += dy;
}
void moveRhomb(struct Rhomb *s, int dx, int dy) {
	s->center_.x += dx;
	s->center_.y += dy;
}

void moveShapes(Shape** shapes, int n, int dx, int dy){
	for (int i=0; i<n; ++i){
		struct Shape* s = shapes[i];
		switch (s->type_){
			case Shape::square: {
				moveSquare( (Square *) s, dx, dy );
				break;
					    }
			case Shape::circle: {
				moveCircle( (Circle *) s, dx, dy );
				break;
					    }
			case Shape::rhomb: {
				moveRhomb( (Rhomb *) s, dx, dy );
				break;
					    }
			default:
				assert(0); 
				exit(0);
		}
	}
}

void drawShapes(Shape** shapes, int n){
	for (int i=0; i<n; ++i){
		struct Shape* s = shapes[i];
		switch (s->type_){
			case Shape::square:
				drawSquare((struct Square*)s);
				break;
			case Shape::circle:
				drawCircle((struct Circle*)s);
				break;
			case Shape::rhomb:
				drawRhomb((struct Rhomb*)s);
				break;
			default:
				assert(0); 
				exit(0);
		}
	}
}
int main(){
	Shape* shapes[5];
	shapes[0]=(Shape*)new Circle;
	shapes[0]->type_=Shape::circle;
	shapes[1]=(Shape*)new Square;
	shapes[1]->type_=Shape::square;
	shapes[2]=(Shape*)new Square;
	shapes[2]->type_=Shape::square;
	shapes[3]=(Shape*)new Circle;
	shapes[4]->type_=Shape::circle;
	shapes[4]=(Shape*)new Rhomb;
	shapes[4]->type_=Shape::rhomb;

	drawShapes(shapes, 5);
	moveShapes(shapes, 5, 10, 20);
	drawShapes(shapes, 5);

}
