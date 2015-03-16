package eu.alfred.personalization_manager.db_administrator.model;

import java.util.UUID;


public class HealthProfile {
	
	private String id;
	private String userID;
	private BloodType bloodType;;
	private String[] chronicalConditions;
	private String[] medicationsTaking;
	private String[] knownAllergies;
	private String[] notableMedicalIncidents;
	private String[] surgeries;
	private String[] vaccinations;
	private ModifiedRankinScale abilityLevel;;
	private String[] dietaryRequirements;
	private String pulseAtRest;
	private String breathingRateAtRest;
	private String bodyTemperatureAtRest;
	private String selfRatedHealth;
	private VisionAcuityScale visionAcuityScore;
	private HearingAcuityScale hearingAcuityScore;
	private LifestyleHealthRelated[] lifestyleHealthRelated;
	private float weight;
	private float height; 
	private PainLevels painLevel;
	private String functionalReachScore;
	private String fiveXSitToStandTestScore;
	private String timedUpAndGoTestScore;
	private String manualDynamometerGripStrengthScore;
	private HandStrengthLevels handStrengthLevel;
	private String sixMinuteWalkTestScore;
	private MuscleGradingScale muscleGrading;
	private BarthelIndex dailyLivingActivitiesScore;
	private MiniMentalStateScore cognitionLevel;
	private SocietyParticipationScale societyParticipationScore;
	private PhysicalActivityLevels physicalActivityLevel;
	private MorseFallRiskScale fallRiskScore;
	private RangeOfMotionScore rangeOfMotionScore;
	
	public HealthProfile () {
		setId();
	}
	
	public String getId() {
		return id;
	}
	public void setId() {
		this.id = "alfred-user-healthProfile-"+UUID.randomUUID().toString(); 
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public BloodType getBloodType() {
		return bloodType;
	}
	public void setBloodType(BloodType bloodType) {
		this.bloodType = bloodType;
	}
	public String[] getChronicalConditions() {
		return chronicalConditions;
	}
	public void setChronicalConditions(String[] chronicalConditions) {
		this.chronicalConditions = chronicalConditions;
	}
	public String[] getMedicationsTaking() {
		return medicationsTaking;
	}
	public void setMedicationsTaking(String[] medicationsTaking) {
		this.medicationsTaking = medicationsTaking;
	}
	public String[] getKnownAllergies() {
		return knownAllergies;
	}
	public void setKnownAllergies(String[] knownAllergies) {
		this.knownAllergies = knownAllergies;
	}
	public String[] getNotableMedicalIncidents() {
		return notableMedicalIncidents;
	}
	public void setNotableMedicalIncidents(String[] notableMedicalIncidents) {
		this.notableMedicalIncidents = notableMedicalIncidents;
	}
	public String[] getSurgeries() {
		return surgeries;
	}
	public void setSurgeries(String[] surgeries) {
		this.surgeries = surgeries;
	}
	public String[] getVaccinations() {
		return vaccinations;
	}
	public void setVaccinations(String[] vaccinations) {
		this.vaccinations = vaccinations;
	}
	public ModifiedRankinScale getAbilityLevel() {
		return abilityLevel;
	}
	public void setAbilityLevel(ModifiedRankinScale abilityLevel) {
		this.abilityLevel = abilityLevel;
	}
	public String[] getDietaryRequirements() {
		return dietaryRequirements;
	}
	public void setDietaryRequirements(String[] dietaryRequirements) {
		this.dietaryRequirements = dietaryRequirements;
	}
	public String getPulseAtRest() {
		return pulseAtRest;
	}
	public void setPulseAtRest(String pulseAtRest) {
		this.pulseAtRest = pulseAtRest;
	}
	public String getBreathingRateAtRest() {
		return breathingRateAtRest;
	}
	public void setBreathingRateAtRest(String breathingRateAtRest) {
		this.breathingRateAtRest = breathingRateAtRest;
	}
	public String getBodyTemperatureAtRest() {
		return bodyTemperatureAtRest;
	}
	public void setBodyTemperatureAtRest(String bodyTemperatureAtRest) {
		this.bodyTemperatureAtRest = bodyTemperatureAtRest;
	}
	public String getSelfRatedHealth() {
		return selfRatedHealth;
	}
	public void setSelfRatedHealth(String selfRatedHealth) {
		this.selfRatedHealth = selfRatedHealth;
	}
	public VisionAcuityScale getVisionAcuityScore() {
		return visionAcuityScore;
	}
	public void setVisionAcuityScore(VisionAcuityScale visionAcuityScore) {
		this.visionAcuityScore = visionAcuityScore;
	}
	public HearingAcuityScale getHearingAcuityScore() {
		return hearingAcuityScore;
	}
	public void setHearingAcuityScore(HearingAcuityScale hearingAcuityScore) {
		this.hearingAcuityScore = hearingAcuityScore;
	}
	public LifestyleHealthRelated[] getLifestyleHealthRelated() {
		return lifestyleHealthRelated;
	}
	public void setLifestyleHealthRelated(
			LifestyleHealthRelated[] lifestyleHealthRelated) {
		this.lifestyleHealthRelated = lifestyleHealthRelated;
	}
	public float getWeight() {
		return weight;
	}
	public void setWeight(float weight) {
		this.weight = weight;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public PainLevels getPainLevel() {
		return painLevel;
	}
	public void setPainLevel(PainLevels painLevel) {
		this.painLevel = painLevel;
	}
	public String getFunctionalReachScore() {
		return functionalReachScore;
	}
	public void setFunctionalReachScore(String functionalReachScore) {
		this.functionalReachScore = functionalReachScore;
	}
	public String getFiveXSitToStandTestScore() {
		return fiveXSitToStandTestScore;
	}
	public void setFiveXSitToStandTestScore(String fiveXSitToStandTestScore) {
		this.fiveXSitToStandTestScore = fiveXSitToStandTestScore;
	}
	public String getTimedUpAndGoTestScore() {
		return timedUpAndGoTestScore;
	}
	public void setTimedUpAndGoTestScore(String timedUpAndGoTestScore) {
		this.timedUpAndGoTestScore = timedUpAndGoTestScore;
	}
	public String getManualDynamometerGripStrengthScore() {
		return manualDynamometerGripStrengthScore;
	}
	public void setManualDynamometerGripStrengthScore(
			String manualDynamometerGripStrengthScore) {
		this.manualDynamometerGripStrengthScore = manualDynamometerGripStrengthScore;
	}
	public HandStrengthLevels getHandStrengthLevel() {
		return handStrengthLevel;
	}
	public void setHandStrengthLevel(HandStrengthLevels handStrengthLevel) {
		this.handStrengthLevel = handStrengthLevel;
	}
	public String getSixMinuteWalkTestScore() {
		return sixMinuteWalkTestScore;
	}
	public void setSixMinuteWalkTestScore(String sixMinuteWalkTestScore) {
		this.sixMinuteWalkTestScore = sixMinuteWalkTestScore;
	}
	public MuscleGradingScale getMuscleGrading() {
		return muscleGrading;
	}
	public void setMuscleGrading(MuscleGradingScale muscleGrading) {
		this.muscleGrading = muscleGrading;
	}
	public BarthelIndex getDailyLivingActivitiesScore() {
		return dailyLivingActivitiesScore;
	}
	public void setDailyLivingActivitiesScore(
			BarthelIndex dailyLivingActivitiesScore) {
		this.dailyLivingActivitiesScore = dailyLivingActivitiesScore;
	}
	public MiniMentalStateScore getCognitionLevel() {
		return cognitionLevel;
	}
	public void setCognitionLevel(MiniMentalStateScore cognitionLevel) {
		this.cognitionLevel = cognitionLevel;
	}
	public SocietyParticipationScale getSocietyParticipationScore() {
		return societyParticipationScore;
	}
	public void setSocietyParticipationScore(
			SocietyParticipationScale societyParticipationScore) {
		this.societyParticipationScore = societyParticipationScore;
	}
	public PhysicalActivityLevels getPhysicalActivityLevel() {
		return physicalActivityLevel;
	}
	public void setPhysicalActivityLevel(
			PhysicalActivityLevels physicalActivityLevel) {
		this.physicalActivityLevel = physicalActivityLevel;
	}
	public MorseFallRiskScale getFallRiskScore() {
		return fallRiskScore;
	}
	public void setFallRiskScore(MorseFallRiskScale fallRiskScore) {
		this.fallRiskScore = fallRiskScore;
	}
	public RangeOfMotionScore getRangeOfMotionScore() {
		return rangeOfMotionScore;
	}
	public void setRangeOfMotionScore(RangeOfMotionScore rangeOfMotionScore) {
		this.rangeOfMotionScore = rangeOfMotionScore;
	}
	
	@Override
	public String toString() {
		return "id: " + getId() + ", userID: " + getUserID() + ", bloodType: " + getBloodType() + " ...";
	}
}
