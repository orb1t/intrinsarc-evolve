module unittests_inference

open structure
open facts
open stratum_help

run simplePropagation1 for 4 but exactly 1 Stratum, exactly 5 Element, exactly 4 Port, exactly 2 Part, exactly 3 Component, exactly 2 Interface, exactly 2 Connector, 7 LinkEnd
pred simplePropagation1
{
	Model::noErrorsAllowed[]
	Model::providesIsNotOptional[]

	some disj x, y, z: Component,
	     disj a, b: Interface
	{
		no optional
		Port.mandatory = Zero
		no Attribute
		no redefines
	
		-- set up the interfaces
		b.resembles = a
	
		-- set up the components
		(x + y).isComposite in False
		some x.links
		z.isComposite in True
		no (x + y + z).resembles
		
		-- define x
		some disj p1, p2: Port
		{
			p1 + p2 = x.ports.Stratum
			no p1.setRequired
			p1.setProvided = a
			no p2.setProvided
			p2.setRequired = a
		}
		
		-- define y
		one p: Port
		{
			p = y.ports.Stratum
			no p.setProvided
			p.setRequired = b
		}
		
		one p: Port | some p1, p2: Part
		{
			p = z.ports.Stratum
			no p.(setRequired + setProvided)
			z.parts.Stratum = p1 + p2
			p1.partType = x
			p2.partType = y
		}
	}
}

run simplePropagation2 for 5 but exactly 1 Stratum, exactly 5 Element, exactly 4 Port, exactly 3 Part, exactly 3 Component, exactly 2 Interface, exactly 3 Connector, 9 LinkEnd, 6 ConnectorEnd
pred simplePropagation2
{
	Model::noErrorsAllowed[]
	Model::providesIsNotOptional[]

	some disj x, y, z: Component,
	     disj a, b: Interface
	{
		no optional
		Port.mandatory = Zero
		no Attribute
		no redefines
	
		-- set up the interfaces
		b.resembles = a
	
		-- set up the components
		(x + y).isComposite in False
		some x.links
		z.isComposite in True
		no (x + y + z).resembles
		
		-- define x
		some disj p1, p2: Port
		{
			p1 + p2 = x.ports.Stratum
			no p1.setRequired
			p1.setProvided = a
			no p2.setProvided
			p2.setRequired = a
		}
		
		-- define y
		one p: Port
		{
			p = y.ports.Stratum
			no p.setProvided
			p.setRequired = b
		}
		
		one p: Port | some disj p1, p2, p3: Part
		{
			p = z.ports.Stratum
			no p.(setRequired + setProvided)
			z.parts.Stratum = p1 + p2 + p3
			p1.partType = x
			p2.partType = y
			p3.partType = x
		}
	}
}

run simplePropagation2a for 5 but exactly 1 Stratum, exactly 6 Element, exactly 4 Port, exactly 3 Part, exactly 3 Component, exactly 3 Interface, exactly 3 Connector, 9 LinkEnd, 6 ConnectorEnd
pred simplePropagation2a
{
	Model::noErrorsAllowed[]
	Model::providesIsNotOptional[]

	some disj x, y, z: Component,
	     disj a, b, c: Interface
	{
		no optional
		Port.mandatory = Zero
		no Attribute
		no redefines
	
		-- set up the interfaces
		b.resembles = a
		c.resembles = b
	
		-- set up the components
		(x + y).isComposite in False
		some x.links
		z.isComposite in True
		no (x + y + z).resembles
		
		-- define x
		some disj p1, p2: Port
		{
			p1 + p2 = x.ports.Stratum
			no p1.setRequired
			p1.setProvided = a
			no p2.setProvided
			p2.setRequired = a
		}
		
		-- define y
		one p: Port
		{
			p = y.ports.Stratum
			no p.setProvided
			p.setRequired = b
		}
		
		one p: Port | some disj p1, p2, p3: Part
		{
			p = z.ports.Stratum
			no p.(setRequired + setProvided)
			z.parts.Stratum = p1 + p2 + p3
			p1.partType = x
			p2.partType = y
			p3.partType = x
			p.required.z.Stratum = c
		}
	}
}

run simplePropagation3 for 6 but exactly 1 Stratum, exactly 6 Element, exactly 6 Port, exactly 3 Part, exactly 4 Component, exactly 3 Interface, 4 Connector, 11 LinkEnd, 8 ConnectorEnd
pred simplePropagation3
{
	Model::noErrorsAllowed[]
	Model::providesIsNotOptional[]

	some disj w, x, y, z: Component,
	     disj a, b, c: Interface
	{
		no optional
		Port.mandatory = Zero
		no Attribute
		no redefines
	
		-- set up the interfaces
		b.resembles = a
		no c.resembles
		no resembles.c
	
		-- set up the components
		(x + y).isComposite in False
		some x.links
		z.isComposite in True
		no (x + y + z).resembles
		
		-- define x
		some disj p1, p2, p3: Port
		{
			no p1.setRequired
			p1.setProvided = a
			no p2.setProvided
			p2.setRequired = a
			p3.setProvided = c
			no p3.setRequired
		}
		
		-- define y
		one p: Port
		{
			p = y.ports.Stratum
			no p.setProvided
			p.setRequired = b
		}
		
		-- define w
		one p: Port
		{
			p = w.ports.Stratum
			no p.setRequired
			p.setProvided = b
		}
		
		one p: Port | some disj p1, p2, p3: Part
		{
			p = z.ports.Stratum
			no p.(setRequired + setProvided)
			z.parts.Stratum = p1 + p2 + p3
			p1.partType = w
			p2.partType = y
			p3.partType = x
		}
	}
}

run simplePropagation3a for 6 but exactly 1 Stratum, exactly 6 Element, exactly 6 Port, exactly 3 Part, exactly 4 Component, exactly 3 Interface, 4 Connector, 11 LinkEnd, 8 ConnectorEnd
pred simplePropagation3a
{
	Model::noErrorsAllowed[]
	Model::providesIsNotOptional[]

	some disj w, x, y, z: Component,
	     disj a, b, c: Interface
	{
		no optional
		Port.mandatory = Zero
		no Attribute
		no redefines
	
		-- set up the interfaces
		b.resembles = a
		no b.superTypes.Stratum
		no c.resembles
		no resembles.c
	
		-- set up the components
		(x + y).isComposite in False
		some x.links
		z.isComposite in True
		no (x + y + z).resembles
		
		-- define x
		some disj p1, p2, p3: Port
		{
			no p1.setRequired
			p1.setProvided = a
			no p2.setProvided
			p2.setRequired = a
			p3.setProvided = c
			no p3.setRequired
		}
		
		-- define y
		one p: Port
		{
			p = y.ports.Stratum
			no p.setProvided
			p.setRequired = b
		}
		
		-- define w
		one p: Port
		{
			p = w.ports.Stratum
			no p.setRequired
			p.setProvided = b
		}
		
		one p: Port | some disj p1, p2, p3: Part
		{
			p = z.ports.Stratum
			no p.(setRequired + setProvided)
			z.parts.Stratum = p1 + p2 + p3
			p1.partType = w
			p2.partType = y
			p3.partType = x
		}
	}
}

run simplePropagation4 for 6 but exactly 1 Stratum, exactly 6 Element, exactly 6 Port, exactly 3 Part, exactly 4 Component, exactly 4 Interface, 4 Connector, 11 LinkEnd, 8 ConnectorEnd
pred simplePropagation4
{
	Model::noErrorsAllowed[]
	Model::providesIsNotOptional[]

	some disj w, x, y, z: Component,
	     disj a, b, c, d: Interface
	{
		no optional
		Port.mandatory = Zero
		no Attribute
		no redefines
	
		-- set up the interfaces
		b.resembles = a
		no c.resembles
		no resembles.c
		d.resembles = a
	
		-- set up the components
		(x + y).isComposite in False
		some x.links
		z.isComposite in True
		no (x + y + z).resembles
		
		-- define x
		some disj p1, p2, p3: Port
		{
			no p1.setRequired
			p1.setProvided = a
			no p2.setProvided
			p2.setRequired = a
			p3.setProvided = c
			no p3.setRequired
		}
		
		-- define y
		one p: Port
		{
			p = y.ports.Stratum
			no p.setProvided
			p.setRequired = b
		}
		
		-- define w
		one p: Port
		{
			p = w.ports.Stratum
			no p.setRequired
			p.setProvided = d
		}
		
		one p: Port | some disj p1, p2, p3: Part
		{
			p = z.ports.Stratum
			no p.(setRequired + setProvided)
			z.parts.Stratum = p1 + p2 + p3
			p1.partType = w
			p2.partType = y
			p3.partType = x
		}
	}
}

run simplePropagation5 for 4 but exactly 1 Stratum, exactly 5 Element, exactly 4 Port, exactly 2 Part, exactly 3 Component, exactly 2 Interface, exactly 2 Connector, 7 LinkEnd
pred simplePropagation5
{
	Model::noErrorsAllowed[]
	Model::providesIsNotOptional[]

	some disj x, y, z: Component,
	     disj a, b: Interface
	{
		no optional
		Port.mandatory = Zero
		no Attribute
		no redefines
	
		-- set up the interfaces
		b.resembles = a
	
		-- set up the components
		(x + y).isComposite in False
		some x.links
		z.isComposite = True
		(x + y).isComposite = False
		no (x + y + z).resembles
		
		-- define x
		some disj p1, p2: Port
		{
			p1 + p2 = x.ports.Stratum
			no p1.setRequired
			p1.setProvided = a
			no p2.setProvided
			p2.setRequired = a
		}
		
		-- define y
		one p: Port
		{
			p = y.ports.Stratum
			no p.setRequired
			p.setProvided = b
		}
		
		one p: Port | some p1, p2: Part
		{
			p = z.ports.Stratum
			no p.(setRequired + setProvided)
			z.parts.Stratum = p1 + p2
			p1.partType = x
			p2.partType = y
			no p.required.z.Stratum
		}
	}
}


run simplePropagation6 for 6 but exactly 1 Stratum, exactly 5 Element, exactly 6 Port, exactly 2 Part, exactly 3 Component, exactly 2 Interface, exactly 3 Connector, 10 LinkEnd, 6 ConnectorEnd
pred simplePropagation6
{
	Model::noErrorsAllowed[]
	Model::providesIsNotOptional[]

	some disj x, y, z: Component,
	     disj a, b: Interface
	{
		no optional
		Port.mandatory = Zero
		no Attribute
		no redefines
	
		-- set up the interfaces
		b.resembles = a		
	
		-- set up the components
		(x + y).isComposite in False
		some x.links
		some y.links
		z.isComposite in True
		no (x + y + z).resembles
		
		-- define x
		some disj p1, p2: Port
		{
			p1 + p2 = x.ports.Stratum
			no p1.setRequired
			p1.setProvided = a
			no p2.setProvided
			p2.setRequired = a
		}
		
		-- define x
		some disj p1, p2: Port
		{
			p1 + p2 = y.ports.Stratum
			no p1.setRequired
			p1.setProvided = b
			no p2.setProvided
			p2.setRequired = b
		}
		
		some port1, port2: Port | some disj p1, p2: Part
		{
			z.ports.Stratum = port1 + port2
			no (port1 + port2).(setRequired + setProvided)
			z.parts.Stratum = p1 + p2
			p1.partType = x
			p2.partType = y
		}
	}
}

run simplePropagation6a for 6 but exactly 1 Stratum, exactly 6 Element, exactly 6 Port, exactly 2 Part, exactly 3 Component, exactly 3 Interface, exactly 3 Connector, 10 LinkEnd, 6 ConnectorEnd
pred simplePropagation6a
{
	Model::noErrorsAllowed[]
	Model::providesIsNotOptional[]

	some disj x, y, z: Component,
	     disj a, b, c: Interface
	{
		no optional
		Port.mandatory = Zero
		no Attribute
		no redefines
	
		-- set up the interfaces
		b.resembles = a
		c.resembles = b
	
		-- set up the components
		(x + y).isComposite in False
		some x.links
		z.isComposite in True
		no (x + y + z).resembles
		
		-- define x
		some disj p1, p2: Port
		{
			p1 + p2 = x.ports.Stratum
			no p1.setRequired
			p1.setProvided = a
			no p2.setProvided
			p2.setRequired = a
		}
		
		-- define x
		some disj p1, p2: Port
		{
			p1 + p2 = y.ports.Stratum
			no p1.setRequired
			p1.setProvided = b
			no p2.setProvided
			p2.setRequired = b
		}
		
		some port1, port2: Port | some disj p1, p2: Part
		{
			z.ports.Stratum = port1 + port2
			no (port1 + port2).(setRequired + setProvided)
			z.parts.Stratum = p1 + p2
			p1.partType = x
			p2.partType = y
			port1.required.z.Stratum = c
		}
	}
}

run simplePropagation7 for 6 but exactly 1 Stratum, exactly 6 Port, exactly 3 Part, exactly 4 Component, exactly 3 Interface, exactly 7 Element, 4 Connector, 11 LinkEnd, 8 ConnectorEnd
pred simplePropagation7
{
	Model::noErrorsAllowed[]
	Model::providesIsNotOptional[]

	some disj w, x, y, z: Component,
	     disj a, b, c: Interface
	{
		no optional
		Port.mandatory = Zero
		no Attribute
		no redefines
	
		-- set up the interfaces
		b.resembles = a
		no c.resembles
		no resembles.c
	
		-- set up the components
		(x + y).isComposite in False
		some x.links
		z.isComposite in True
		no (x + y + z).resembles
		
		-- define x
		some disj p1, p2, p3: Port
		{
			no p1.setRequired
			p1.setProvided = a
			no p2.setProvided
			p2.setRequired = a
			p3.setProvided = c
			no p3.setRequired
		}
		
		-- define y
		one p: Port
		{
			p = y.ports.Stratum
			no p.setProvided
			p.setRequired = b
		}
		
		-- define w
		one p: Port
		{
			p = w.ports.Stratum
			no p.setRequired
			p.setProvided = a
		}
		
		one p: Port | some disj p1, p2, p3: Part
		{
			p = z.ports.Stratum
			no p.(setRequired + setProvided)
			z.parts.Stratum = p1 + p2 + p3
			p1.partType = w
			p2.partType = y
			p3.partType = x
		}
	}
}

run simplePropagation8 for 6 but exactly 1 Stratum, exactly 5 Port, exactly 3 Part, exactly 4 Component, exactly 2 Interface, exactly 6 Element, 3 Connector, 9 LinkEnd, 6 ConnectorEnd
pred simplePropagation8
{
	Model::noErrorsAllowed[]
	Model::providesIsNotOptional[]

	some disj w, x, y, z: Component,
	     disj a, b: Interface
	{
		no optional
		no Attribute
		no redefines
	
		-- set up the interfaces
		b.resembles = a
	
		-- set up the components
		(x + y).isComposite in False
		some x.links
		z.isComposite in True
		no (x + y + z).resembles
		
		-- define x
		some disj p1, p2: Port
		{
			no p1.setRequired
			p1.setProvided = a
			no p2.setProvided
			p2.setRequired = a
		}
		
		-- define y
		one p: Port
		{
			p = y.ports.Stratum
			no p.setRequired
			p.setProvided = a
		}
		
		-- define w
		one p: Port
		{
			p = w.ports.Stratum
			no p.setRequired
			p.setProvided = b
		}
		
		one p: Port | some disj p1, p2, p3: Part
		{
			p = z.ports.Stratum
			no p.(setRequired + setProvided)
			z.parts.Stratum = p1 + p2 + p3
			p1.partType = w
			p2.partType = y
			no (p1 + p2).linkedToOutside
			p3.partType = x			
		}
	}
}

run simplePropagation9 for 6 but exactly 1 Stratum, exactly 5 Port, exactly 3 Part, exactly 4 Component, exactly 2 Interface, exactly 6 Element, 3 Connector, 9 LinkEnd, 6 ConnectorEnd
pred simplePropagation9
{
	Model::noErrorsAllowed[]
	Model::providesIsNotOptional[]

	some disj w, x, y, z: Component,
	     disj a, b: Interface
	{
		no optional
		no Attribute
		no redefines
	
		-- set up the interfaces
		b.resembles = a
	
		-- set up the components
		(x + y + w).isComposite in False
		some x.links
		z.isComposite in True
		no (w + x + y + z).resembles
		
		-- define x
		some disj p1, p2: Port
		{
			no p1.setRequired
			p1.setProvided = a
			no p2.setProvided
			p2.setRequired = a
		}
		
		-- define y
		one p: Port
		{
			p = y.ports.Stratum
			no p.setProvided
			p.setRequired = a
		}
		
		-- define w
		one p: Port
		{
			p = w.ports.Stratum
			no p.setProvided
			p.setRequired = b
		}
		
		one p: Port | some disj p1, p2, p3: Part
		{
			p = z.ports.Stratum
			no p.(setRequired + setProvided)
			z.parts.Stratum = p1 + p2 + p3
			p1.partType = w
			p2.partType = y
			no (p1 + p2).linkedToOutside
			p3.partType = x
		}
	}
}

run simplePropagation10 for 4 but exactly 1 Stratum, 6 LinkEnd
pred simplePropagation10
{
	Model::noErrorsAllowed[]
	Model::providesIsNotOptional[]

	some disj x, z: Component,
	     disj a, b: Interface
	{
		no optional
		Port.mandatory = Zero
		no Attribute
		no redefines
	
		-- set up the interfaces
		b.resembles = a
	
		-- set up the components
		isFalse[x.isComposite]
		some x.links
		isTrue[z.isComposite]
		no (x + z).resembles
		
		-- define x
		some disj p1, p2: Port
		{
			p1 + p2 = x.ports.Stratum
			no p1.setRequired
			p1.setProvided = a
			no p2.setProvided
			p2.setRequired = a
		}
		
		some port1, port2: Port | some p1: Part
		{
			port1 + port2 = z.ports.Stratum
			no (port1 + port2).(setRequired + setProvided)
			z.parts.Stratum = p1
			p1.partType = x
			no port1.required.z.Stratum
			port1.provided.z.Stratum = b
			no port2.provided.z.Stratum
			port2.required.z.Stratum = b
		}
	}
}

run simplePropagation11 for 4 but exactly 1 Stratum, exactly 3 Port, exactly 2 Part, exactly 3 Component, exactly 3 Interface, exactly 6 Element, 2 Connector, 6 LinkEnd, 4 ConnectorEnd
pred simplePropagation11
{
	Model::noErrorsAllowed[]
	Model::providesIsNotOptional[]

	some disj x, y, z: Component,
	     disj a, b, c: Interface
	{
		no optional
		no Attribute
		no redefines
	
		-- set up the interfaces
		no (a + b).resembles
		c.resembles = a + b
		c.superTypes.Stratum = a + b
		
	
		-- set up the components
		(x + y).isComposite in False
		z.isComposite in True
		no (x + y + z).resembles
		
		-- define x
		one p: Port
		{
			p = x.ports.Stratum
			no p.setProvided
			p.setRequired = a
		}
		
		-- define y
		one p: Port
		{
			p = y.ports.Stratum
			no p.setProvided
			p.setRequired = b
		}
		
		one p: Port | some disj p1, p2: Part
		{
			p = z.ports.Stratum
			no p.(setRequired + setProvided)
			z.parts.Stratum = p1 + p2
			p1.partType = x
			p2.partType = y
		}
		
		no linkedToParts
	}
}


run simplePropagation12 for 4 but exactly 1 Stratum, exactly 3 Element, exactly 2 Port, exactly 1 Part, exactly 2 Component, exactly 2 Interface
pred simplePropagation12
{
	Model::noErrorsAllowed[]
	Model::providesIsNotOptional[]

	some disj y, z: Component,
	     disj a, b: Interface
	{
		no optional
		Port.mandatory = Zero
		no Attribute
		no redefines
	
		-- set up the interfaces
		b.resembles = a
	
		-- set up the components
		y.isComposite in False
		z.isComposite in True
		no (y + z).resembles
		
		-- define y
		one p: Port
		{
			p = y.ports.Stratum
			no p.setRequired
			p.setProvided = b
		}
		
		one p: Port | some p1: Part
		{
			p = z.ports.Stratum
			no p.(setRequired + setProvided)
			z.parts.Stratum = p1
			p1.partType = y
			p.provided.z.Stratum = a
		}
	}
}

run simplePropagation13 for 4 but exactly 1 Stratum, exactly 3 Element, exactly 2 Port, exactly 1 Part, exactly 2 Component, exactly 2 Interface
pred simplePropagation13
{
	Model::noErrorsAllowed[]
	Model::providesIsNotOptional[]

	some disj y, z: Component,
	     disj a, b: Interface
	{
		no optional
		Port.mandatory = Zero
		no Attribute
		no redefines
	
		-- set up the interfaces
		b.resembles = a
	
		-- set up the components
		y.isComposite in False
		z.isComposite in True
		no (y + z).resembles
		
		-- define y
		one p: Port
		{
			p = y.ports.Stratum
			no p.setProvided
			p.setRequired = a
		}
		
		one p: Port | some p1: Part
		{
			p = z.ports.Stratum
			no p.(setRequired + setProvided)
			z.parts.Stratum = p1
			p1.partType = y
			p.required.z.Stratum = b
		}
	}
}

run simplePropagation14 for 4 but exactly 1 Stratum, exactly 5 Element, exactly 4 Port, exactly 2 Part, exactly 3 Component, exactly 2 Interface, exactly 2 Connector, 7 LinkEnd
pred simplePropagation14
{
	Model::noErrorsAllowed[]
	Model::providesIsNotOptional[]

	some disj x, y, z: Component,
	     disj a, b: Interface
	{
		no optional
		Port.mandatory = Zero
		no Attribute
		no redefines
	
		-- set up the interfaces
		b.resembles = a
	
		-- set up the components
		(x + y).isComposite in False
		some x.links
		z.isComposite in True
		no (x + y + z).resembles
		
		-- define x
		some disj p1, p2: Port
		{
			p1 + p2 = x.ports.Stratum
			p1.setRequired = a
			p1.setProvided = b
			p2.setRequired = b
			p2.setProvided = a
		}
		
		-- define y
		one p: Port
		{
			p = y.ports.Stratum
			p.setProvided = b
			p.setRequired = a
		}
		
		one p: Port | some p1, p2: Part
		{
			p = z.ports.Stratum
			no p.(setRequired + setProvided)
			z.parts.Stratum = p1 + p2
			p1.partType = x
			p2.partType = y
			no p2.linkedToOutside
		}
	}
}

run simplePropagation15 for 6 but exactly 1 Stratum, exactly 5 Element, exactly 5 Port, exactly 2 Part, exactly 3 Component, exactly 2 Interface, exactly 3 Connector, 8 LinkEnd
pred simplePropagation15
{
	Model::noErrorsAllowed[]
	Model::providesIsNotOptional[]

	some disj x, y, z: Component,
	     disj a, b: Interface
	{
		no optional
		no Attribute
		no redefines
	
		-- set up the interfaces
		b.resembles = a
	
		-- set up the components
		(x + y).isComposite in False
		some x.links
		z.isComposite in True
		no (x + y + z).resembles
		
		-- define x
		some disj p1, p2: Port
		{
			p1 + p2 = x.ports.Stratum
			no p1.setRequired
			p1.setProvided = a
			p1.mandatory = Zero
			no p2.setProvided
			p2.setRequired = a
			p2.mandatory = Zero + One
		}
		
		-- define y
		one p: Port
		{
			p = y.ports.Stratum
			p.setProvided = b
			no p.setRequired
			p.mandatory = Zero
		}
		
		some disj port1, port2: Port | some p1, p2: Part
		{
			port1 + port2 = z.ports.Stratum
			(port1 + port2).mandatory = Zero
			no (port1 + port2).(setRequired + setProvided)
			z.parts.Stratum = p1 + p2
			p1.partType = x
			p2.partType = y
			no p2.linkedToOutside
		}
	}
}


run simplePropagation16 for 5 but exactly 1 Stratum, exactly 6 Element, exactly 4 Port, exactly 2 Part, exactly 3 Component, exactly 3 Interface, exactly 2 Connector, 7 LinkEnd
pred simplePropagation16
{
	Model::noErrorsAllowed[]
	Model::providesIsNotOptional[]

	some disj x, y, z: Component,
	     disj a, b, c: Interface
	{
		no optional
		Port.mandatory = Zero
		no Attribute
		no redefines
	
		-- set up the interfaces
		b.resembles = a
		no c.resembles
		no a.resembles
	
		-- set up the components
		(x + y).isComposite in False
		some x.links
		z.isComposite = True
		(x + y).isComposite = False
		no (x + y + z).resembles
		
		-- define x
		some disj p1, p2: Port
		{
			p1 + p2 = x.ports.Stratum
			no p1.setRequired
			p1.setProvided = a
			no p2.setProvided
			p2.setRequired = a
		}
		
		-- define y
		one p: Port
		{
			p = y.ports.Stratum
			no p.setRequired
			p.setProvided = b + c
		}
		
		one p: Port | some p1, p2: Part
		{
			p = z.ports.Stratum
			no p.(setRequired + setProvided)
			z.parts.Stratum = p1 + p2
			p1.partType = x
			p2.partType = y
			no p.required.z.Stratum
		}
	}
}

