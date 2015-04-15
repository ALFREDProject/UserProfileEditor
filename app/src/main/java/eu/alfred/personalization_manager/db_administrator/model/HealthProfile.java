package eu.alfred.personalization_manager.db_administrator.model;

import java.util.Date;
import java.util.UUID;


public class HealthProfile {
	
	private String id;
	private String userID;
	private BloodType bloodType;
	private float weight;
	private float height; 
	private String[] chronicalConditions;
	private VisionAcuityScale visionAcuityScore;
	private HearingAcuityScale hearingAcuityScore; 
	private String[] knownAllergies;
	private String[] dietaryRequirements;
	private String[] surgeries;
	private String[] disabilities;
	private float visualAnalogScaleScore;
	private float miniMentalStateExamScore;
	private float elderlyMobilityScaleScore;
	private float bergBalanceScaleScore;
	private int fiveTimesSitToStandScore;
	private float hanHeldDynamometerScore;
	private float sixMinuteWalkingTestScore;
	private float rangeOfMotionScore;
	private float modifiedFallEfficiencyScaleScore;
	private float lowBackPainScore;
	private int stepsPerDay;
	private float pulseAtRest;
	private float respiratoryRateAtRest;
	private float bodyTemperatureAtRest; 
	private LifestyleHealthRelated[] lifestyleHealthRelated;  
	private SocietyParticipationScale societyParticipationScore; 
	private Date creationDate;
	

	public HealthProfile () {
		setId();
		setCreationDate();
	}
	
	public String getId() {
		return id;
	}
	private void setId() {
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
	public String[] getChronicalConditions() {
		return chronicalConditions;
	}
	public void setChronicalConditions(String[] chronicalConditions) {
		this.chronicalConditions = chronicalConditions;
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
	public String[] getKnownAllergies() {
		return knownAllergies;
	}
	public void setKnownAllergies(String[] knownAllergies) {
		this.knownAllergies = knownAllergies;
	}
	public String[] getDietaryRequirements() {
		return dietaryRequirements;
	}
	public void setDietaryRequirements(String[] dietaryRequirements) {
		this.dietaryRequirements = dietaryRequirements;
	}
	public String[] getSurgeries() {
		return surgeries;
	}
	public void setSurgeries(String[] surgeries) {
		this.surgeries = surgeries;
	}
	public String[] getDisabilities() {
		return disabilities;
	}
	public void setDisabilities(String[] disabilities) {
		this.disabilities = disabilities;
	}
	public float getVisualAnalogScaleScore() {
		return visualAnalogScaleScore;
	}
	public void setVisualAnalogScaleScore(float visualAnalogScaleScore) {
		this.visualAnalogScaleScore = visualAnalogScaleScore;
	}
	public float getMiniMentalStateExamScore() {
		return miniMentalStateExamScore;
	}

	public void setMiniMentalStateExamScore(float miniMentalStateExamScore) {
		this.miniMentalStateExamScore = miniMentalStateExamScore;
	}
	public float getElderlyMobilityScaleScore() {
		return elderlyMobilityScaleScore;
	}
	public void setElderlyMobilityScaleScore(float elderlyMobilityScaleScore) {
		this.elderlyMobilityScaleScore = elderlyMobilityScaleScore;
	}
	public float getBergBalanceScaleScore() {
		return bergBalanceScaleScore;
	}
	public void setBergBalanceScaleScore(float bergBalanceScaleScore) {
		this.bergBalanceScaleScore = bergBalanceScaleScore;
	}
	public int getFiveTimesSitToStandScore() {
		return fiveTimesSitToStandScore;
	}
	public void setFiveTimesSitToStandScore(int fiveTimesSitToStandScore) {
		this.fiveTimesSitToStandScore = fiveTimesSitToStandScore;
	}
	public float getHanHeldDynamometerScore() {
		return hanHeldDynamometerScore;
	}
	public void setHanHeldDynamometerScore(float hanHeldDynamometerScore) {
		this.hanHeldDynamometerScore = hanHeldDynamometerScore;
	}
	public float getSixMinuteWalkingTestScore() {
		return sixMinuteWalkingTestScore;
	}
	public void setSixMinuteWalkingTestScore(float sixMinuteWalkingTestScore) {
		this.sixMinuteWalkingTestScore = sixMinuteWalkingTestScore;
	}
	public float getRangeOfMotionScore() {
		return rangeOfMotionScore;
	}
	public void setRangeOfMotionScore(float rangeOfMotionScore) {
		this.rangeOfMotionScore = rangeOfMotionScore;
	}
	public float getModifiedFallEfficiencyScaleScore() {
		return modifiedFallEfficiencyScaleScore;
	}
	public void setModifiedFallEfficiencyScaleScore(
			float modifiedFallEfficiencyScaleScore) {
		this.modifiedFallEfficiencyScaleScore = modifiedFallEfficiencyScaleScore;
	}
	public float getLowBackPainScore() {
		return lowBackPainScore;
	}
	public void setLowBackPainScore(float lowBackPainScore) {
		this.lowBackPainScore = lowBackPainScore;
	}
	public int getStepsPerDay() {
		return stepsPerDay;
	}
	public void setStepsPerDay(int stepsPerDay) {
		this.stepsPerDay = stepsPerDay;
	}
	public float getPulseAtRest() {
		return pulseAtRest;
	}
	public void setPulseAtRest(float pulseAtRest) {
		this.pulseAtRest = pulseAtRest;
	}
	public float getRespiratoryRateAtRest() {
		return respiratoryRateAtRest;
	}
	public void setRespiratoryRateAtRest(float respiratoryRateAtRest) {
		this.respiratoryRateAtRest = respiratoryRateAtRest;
	}
	public float getBodyTemperatureAtRest() {
		return bodyTemperatureAtRest;
	}
	public void setBodyTemperatureAtRest(float bodyTemperatureAtRest) {
		this.bodyTemperatureAtRest = bodyTemperatureAtRest;
	}
	public LifestyleHealthRelated[] getLifestyleHealthRelated() {
		return lifestyleHealthRelated;
	}
	public void setLifestyleHealthRelated(
			LifestyleHealthRelated[] lifestyleHealthRelated) {
		this.lifestyleHealthRelated = lifestyleHealthRelated;
	}
	public SocietyParticipationScale getSocietyParticipationScore() {
		return societyParticipationScore;
	}
	public void setSocietyParticipationScore(
			SocietyParticipationScale societyParticipationScore) {
		this.societyParticipationScore = societyParticipationScore;
	}
		

	public Date getCreationDate() {
		return creationDate;
	}

	private void setCreationDate() {
		this.creationDate = new Date();
	}

	@Override
	public String toString() {
		return "id: " + getId() + ", userID: " + getUserID() + ", bloodType: " + getBloodType() + " ...";
	}
}
