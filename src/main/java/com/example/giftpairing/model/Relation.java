package com.example.giftpairing.model;

public enum Relation {
	
	SPOUSE {
		@Override
		public Relation reverseRelation() {
			return SPOUSE;
		}
	}, 
	CHILD {
		@Override
		public Relation reverseRelation() {
			return PARENT;
		}
	}, 
	PARENT {
		@Override
		public Relation reverseRelation() {
			return CHILD;
		}
	}, 
	SIBLING {
		@Override
		public Relation reverseRelation() {
			return SIBLING;
		}
	};
	
	abstract public Relation reverseRelation();
}
