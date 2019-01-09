package chav1961.merc.core.robots;


import chav1961.merc.api.Area;
import chav1961.merc.api.Constants;
import chav1961.merc.api.Point;
import chav1961.merc.api.Track;
import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.exceptions.MercEnvironmentException;
import chav1961.merc.api.interfaces.front.ControlInterface;
import chav1961.merc.api.interfaces.front.Entity;
import chav1961.merc.api.interfaces.front.EntityClass;
import chav1961.merc.api.interfaces.front.EntityClassDescription;
import chav1961.merc.api.interfaces.front.MillState;
import chav1961.merc.api.interfaces.front.ResourceClass;
import chav1961.merc.api.interfaces.front.TickableEntity;
import chav1961.merc.api.interfaces.front.World.WorldLocker;
import chav1961.merc.api.interfaces.world.MessageInterface;
import chav1961.merc.api.interfaces.world.ResourceDescriptorInterface;
import chav1961.merc.api.interfaces.world.SeenInterface;
import chav1961.merc.api.interfaces.world.UniversalRobotInterface;
import chav1961.merc.core.buildings.TeleportInstance;
import chav1961.merc.core.buildings.TeleportState;
import chav1961.merc.core.landing.Basement;
import chav1961.merc.core.landing.BasementInstance;
import chav1961.merc.core.landing.BasementState;
import chav1961.merc.core.util.Tools;

public class UniversalRobotControl implements ControlInterface<UniversalRobotState>, UniversalRobotInterface {
	private final UniversalRobotInstance 	instance;
	
	private Object		anyThing = null;

	public UniversalRobotControl(final UniversalRobotInstance instance) {
		this.instance = instance;
	}

	@Override
	public Entity<UniversalRobotState> getEntity() {
		return instance;
	}

	@Override
	public UniversalRobotInterface destroy(final Point point) throws MercContentException, MercEnvironmentException {
		validateLevelAndState(0,UniversalRobotState.Free);
		Tools.validatePointIsPointedTo(instance.getWorld(),point,EntityClass.Mills);
		
		for (Entity<?> item : instance.getWorld().content(EntityClass.Mills,"*")) {
			instance.setState(UniversalRobotState.Destroys);
			((Entity<MillState>)item).setState(MillState.Destroying);
			instance.getWorld().delayGameTime(1000);
			((Entity<MillState>)item).setState(MillState.Destroyed);
			instance.setState(UniversalRobotState.Free);
			instance.getWorld().getPaymentPanel().operationPayment(1.2f);
			break;
		}
		return this;
	}

	@Override
	public boolean hasAnyProbe() {
		return instance.getState() == UniversalRobotState.HasProbe;
	}

	@Override
	public UniversalRobotInterface mill(final Point point, final String millType, final String millName) throws MercContentException, MercEnvironmentException {
		validateLevelAndState(0,UniversalRobotState.Free);
		
		try(WorldLocker	locker = instance.getWorld().lock(getEntity().getId(),point.x,point.y)) {
			Tools.validatePointIsFree(instance.getWorld(),point);
			
			final EntityClassDescription<MillState> desc = (EntityClassDescription<MillState>) instance.getWorld().registered(EntityClass.Mills,millType);
			if (desc == null) {
				throw new MercContentException("Mill type ["+millType+"] is not known in the world");
			}
			else {
				final Entity<MillState>	mill = desc.newInstance(instance.getWorld());
				
				instance.setState(UniversalRobotState.Builds);
				instance.getWorld().placeEntity(mill.setX(point.x).setY(point.y).setState(MillState.Building));
				instance.getWorld().delayGameTime(2000);
				((TickableEntity)mill.setState(MillState.Producing)).setName(millName);
				instance.setState(UniversalRobotState.Free);
				instance.getWorld().getPaymentPanel().operationPayment(10.0f);
			}
		}
		return this;
	}

	@Override
	public UniversalRobotInterface probe(final Point point) throws MercEnvironmentException, MercContentException {
		validateLevelAndState(0,UniversalRobotState.Free);
		Tools.validatePointIsFree(instance.getWorld(),point);

		instance.setState(UniversalRobotState.GatherProbe);
		instance.getWorld().delayGameTime(2000);
		anyThing = point;
		instance.setState(UniversalRobotState.HasProbe);
		instance.getWorld().getPaymentPanel().operationPayment(10.0f);
		
		return this;
	}

	@Override
	public ResourceDescriptorInterface send2Laboratory(final Point point) throws MercEnvironmentException, MercContentException {
		validateLevelAndState(0,UniversalRobotState.HasProbe);
		Tools.validatePointIsPointedTo(instance.getWorld(),point,EntityClass.Buildings,"teleport");
		
		final TeleportInstance	inst = (TeleportInstance) instance.getWorld().getEntity(Constants.TELEPORT_INSTANCE_UUID);  
		
		inst.setState(TeleportState.ProcessCommand);
		instance.setState(UniversalRobotState.Uploading);
		instance.getWorld().delayGameTime(2000);
		
		final ResourceDescriptorInterface 	rdi = inst.getResourceForPoint((Point)anyThing);
		
		anyThing = null;
		inst.setState(TeleportState.Ready);
		instance.setState(UniversalRobotState.Free);
		instance.getWorld().getPaymentPanel().operationPayment(30.0f);
		
		return rdi;
	}

	@Override
	public MessageInterface receiveMessage(final boolean wait) throws MercContentException {
		validateLevel(1);
		for (Entity<?> item : instance.getWorld().content(EntityClass.Buildings,"")) {
			
			return null;
		}
		throw new MercContentException("You have no Radio towers to send/receive messages. Build at lest one RadioTower firstly");
	}

	@Override
	public UniversalRobotInterface sendMessage(final String entityId, final Object message) {
		validateLevel(1);
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface beginExcavation(String pipeType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface beginTrace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface connect(Point point) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface connect(Point pointFrom, Point pointTo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface cut(Point point) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface disconnect(Point point) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface disconnect(Point fromPoint, Point toPoint) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface endExcavation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface endTrace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface download(Point point, String resource) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface download(Point point, ResourceClass resource) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface download(Point point, String resource, float amount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface download(Point point, ResourceClass resource, float amount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float howManyCargo() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UniversalRobotInterface upload(Point point) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResourceClass cargoType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface moveTo(Point... to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface moveTo(Point to, Track road) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SeenInterface<?>[] see(Point point) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SeenInterface<?>[] see(Point point, EntityClass type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point where() {
		return new Point(getEntity().getX(),getEntity().getY());
	}

	@Override
	public UniversalRobotInterface lock(Point... coords) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface lock(Area... coords) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface lock(Track coords) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface unlock(Point... coords) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface unlock(Track coords) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface unlock(Area area) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface capture(Point point) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface place(Point point) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasAnyCaptured() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Entity<?> whatCaptured() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface mount(Point point) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface prepareBasement(final Point point) throws MercContentException, MercEnvironmentException {
		validateLevelAndState(1,UniversalRobotState.Free);
		Tools.validatePointIsLegal(instance.getWorld(),point);
		
		try(final WorldLocker		locker = instance.getWorld().lock(instance.getId(),point.x,point.y)) {
			final Basement			basement = (Basement) instance.getWorld().registered(EntityClass.Land,Basement.class.getSimpleName());
			
			Tools.validatePointIsFree(instance.getWorld(),point);
			final BasementInstance	inst = (BasementInstance) basement.newInstance(instance.getWorld());
			
			instance.getWorld().placeEntity(inst.setX(point.x).setY(point.y).setState(BasementState.Building));
			anyThing = point;
			instance.setState(UniversalRobotState.Builds);
			instance.getWorld().delayGameTime(2000);
			instance.setState(UniversalRobotState.Free);
			inst.setState(BasementState.Ready);
			instance.getWorld().getPaymentPanel().operationPayment(30.0f);
		}
		return this;
	}

	@Override
	public UniversalRobotInterface prepareBasement2x2(Point point) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface unmount(Point point) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object ask() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface excavate(int x, int y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface logoff() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface logon(Point point) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UniversalRobotInterface assignName(Point point, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	void tick() {
		
	}

	void validateLevel(final int level) {
		
	}
	
	void validateLevelAndState(final int level, final UniversalRobotState state) {
		
	}
}
